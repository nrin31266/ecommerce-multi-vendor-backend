package com.vanrin05.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vanrin05.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.domain.PAYMENT_STATUS;
import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.exception.AppException;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;
import com.vanrin05.repository.OrderRepository;
import com.vanrin05.repository.PaymentOrderRepository;
import com.vanrin05.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentOrderRepository paymentOrderRepository;
    OrderRepository orderRepository;
    VNPayService vnPayService;

    private String apiKey = "";
    private String apiSecret = "";
    private String stripeSecretKey = "";


    @Override
    public PaymentOrder createPaymentOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setOrders(orders);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId).orElseThrow(() -> new AppException("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
        if (paymentOrder == null) {
            throw new AppException("Payment not found with payment link id: " + paymentId);
        }
        return paymentOrder;
    }

    @Transactional
    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {

        if (paymentOrder.getPaymentOrderStatus().equals(PAYMENT_ORDER_STATUS.PENDING)) {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

            Payment payment = razorpayClient.payments.fetch(paymentId);

            String status = payment.get("status");

            if (status.equals("captured")) {
                Set<Order> orders = paymentOrder.getOrders();

                for (Order order : orders) {
                    order.setPaymentStatus(PAYMENT_STATUS.COMPLETED);
                }

                orderRepository.saveAll(orders);

                paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            } else {
                paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }


        }

        return false;
    }

    @Override
    public String createVNPaymentLink(User user, Long amount, Long orderId, Map<String, String> params) {
        String orderType = params.get("orderType");
        String bankCode = params.get("bankCode");
        String orderInfo = params.get("orderInfo");
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;
        try {
            RazorpayClient client = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLinkResponse = client.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl = paymentLinkResponse.get("short_url");
            String paymentLinkId = paymentLinkResponse.get("id");

            return paymentLinkResponse;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel/" + orderId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("vnd")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("rin payment").build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(sessionCreateParams);



        return session.getUrl();
    }


}
