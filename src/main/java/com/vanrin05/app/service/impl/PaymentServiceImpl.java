package com.vanrin05.app.service.impl;

import com.vanrin05.event.SellerReportEvent;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.repository.OrderRepository;
import com.vanrin05.app.repository.PaymentOrderRepository;
import com.vanrin05.app.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentOrderRepository paymentOrderRepository;
    OrderRepository orderRepository;
    VNPayService vnPayService;
    private String stripeSecretKey = "sk_test_51R52WvPvjKDrWoaF1R2WPy4ivyRAlWcAYsPLO0t0A7ma3bTIPN8HQFbwpPyRwhR848Sem8oggdCyojGfLWgQSiMV00X4eCr53r";
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public PaymentOrder createPaymentOrder(User user, List<Order> orders, PAYMENT_METHOD paymentMethod) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.PENDING);
        paymentOrder.setAmount(amount);
        for (Order order : orders) {
            order.setPaymentOrder(paymentOrder);
        }
        paymentOrder.setOrders(orders);
        paymentOrder.setPaymentMethod(paymentMethod);

        return paymentOrderRepository.save(paymentOrder);
    }



    @Transactional
    @Override
    public Boolean proceedPayment(Long paymentId)  {

        PaymentOrder paymentOrder = findById(paymentId);
        List<Order> orders = paymentOrder.getOrders();


        if (paymentOrder.getPaymentOrderStatus().equals(PAYMENT_ORDER_STATUS.PENDING)) {



            for (Order order : orders) {
                order.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.COMPLETED);
            }

            orderRepository.saveAll(orders);

            paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.SUCCESS);
            paymentOrderRepository.save(paymentOrder);


            kafkaTemplate.send("update-seller-report", SellerReportEvent.builder()
                            .orders(new HashSet<>(orders))
                            .paymentId(paymentId)
                    .build());


        }


        return false;
    }

    @Override
    public String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params) {
        String orderType = params.get("orderType");
        String bankCode = params.get("bankCode");
        String orderInfo = params.get("orderInfo");
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode, paymentId);
    }


    @Override
    public String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment-success/" + paymentId)
                .setCancelUrl("http://localhost:5173/payment-cancel/" + paymentId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("vnd")
                                .setUnitAmount(amount)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Thanh toán đơn hàng " + paymentId)
                                        .build())
                                .build())
                        .build())
                .putMetadata("payment_id", String.valueOf(paymentId))
                .build();

        Session session = Session.create(sessionCreateParams);

        return session.getUrl();
    }

    @Override
    public PaymentOrder findById(Long paymentId) {
        PaymentOrder paymentOrder= paymentOrderRepository.findById(paymentId).orElseThrow(()->new AppException("Payment not found"));
        return paymentOrder;
    }


    @Override
    public List<Order> findAllOrdersInPaymentOrder(PaymentOrder paymentOrder) {
        return orderRepository.findAllByPaymentOrder(paymentOrder);
    }



    @Override
    public List<PaymentOrder> findUserPaymentOrders(User user) {
        return paymentOrderRepository.findByUser(user);
    }


}
