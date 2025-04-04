package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.ORDER_STATUS;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.repository.ProductRepository;
import com.vanrin05.app.service.OrderService;
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
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    ProductRepository productRepository;


    @Override
    public PaymentOrder createPaymentOrder(User user, List<Order> orders, PAYMENT_METHOD paymentMethod) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentExpiry(LocalDateTime.now().plus(15, ChronoUnit.MINUTES));
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
                order.setOrderStatus(ORDER_STATUS.PLACED);
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
                .setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond())
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
    @Transactional
    public PaymentOrder cancelPaymentOrder  (Long paymentId, User user) {
        PaymentOrder paymentOrder = findById(paymentId);
        if(!user.getId().equals(paymentOrder.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
        List<Order> orders = paymentOrder.getOrders();
        for (Order order : orders) {
            order.setOrderStatus(ORDER_STATUS.CANCELLED);
            order.setCancelReason("User cancelled payment");
            List<Product> products = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            }
            productRepository.saveAll(products);
        }

        orderRepository.saveAll(orders);
        return paymentOrderRepository.save(paymentOrder);
    }



    @Scheduled(fixedRate = 60_000) // Chạy mỗi phút
    @Transactional
    public void cancelExpiredPayment() {
        // 1. Lấy danh sách đơn hết hạn (đã quá thời gian hiện tại)
        List<PaymentOrder> expiredPayments = paymentOrderRepository
                .customFindByExpiredOnlinePayment(
                        PAYMENT_ORDER_STATUS.PENDING,
                        LocalDateTime.now()
                );


        Map<Long, Integer> productQuantityToAdd = new HashMap<>();
        List<Order> orders = new ArrayList<>();

        expiredPayments.forEach(payment -> {
            payment.getOrders().forEach(order -> {
                order.setCancelReason("Payment expired");
                order.setOrderStatus(ORDER_STATUS.CANCELLED);
                order.getOrderItems().forEach(item -> {
                    productQuantityToAdd.merge(
                            item.getProduct().getId(),
                            item.getQuantity(),
                            Integer::sum
                    );
                });
                orders.add(order);
            });


            payment.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
        });

        orderRepository.saveAll(orders);
        productQuantityToAdd.forEach(productRepository::incrementProductQuantity);

        // 5. Lưu trạng thái hủy đơn
        paymentOrderRepository.saveAll(expiredPayments);
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
