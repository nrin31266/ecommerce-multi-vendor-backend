package com.vanrin05.app.service.impl;

import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.Payment;

import com.vanrin05.app.repository.*;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.TransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    private final SellerRepository sellerRepository;
    SellerReportRepository sellerReportRepository;
    TransactionService transactionService;
    SellerReportService sellerReportService;
    OrderService orderService;


    @Override
    public Payment createPaymentOrder(User user, Order orders, PAYMENT_METHOD paymentMethod) {
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.PENDING);
        payment.setExpiryDate(LocalDateTime.now().plusHours(12));
        payment.setOrder(orders);
        payment.setUser(user);
        return paymentOrderRepository.save(payment);


    }


    @Override
    @Transactional
    public Payment cancelPaymentOrder(Payment payment, User user, String cancelReason) {

        if (!user.getId().equals(payment.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }


        // Handle order
        orderService.cancelOrder(payment.getOrder(), user, cancelReason);
        // Payment
        payment.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.CANCELLED);
        payment.setExpiryDate(LocalDateTime.now());

        return paymentOrderRepository.save(payment);


    }


    @Scheduled(fixedRate = 180_000)
    @Transactional
    public void cancelExpiredPayment() {
        List<Payment> expiredPayments = paymentOrderRepository.findExpiredPaymentsByStatus(
                PAYMENT_ORDER_STATUS.PENDING, LocalDateTime.now()
        );

        for (Payment expiredPayment : expiredPayments) {
            cancelPaymentOrder(expiredPayment, expiredPayment.getUser(), "Late payment deadline");

            expiredPayment.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.CANCELLED);

        }

        paymentOrderRepository.saveAll(expiredPayments);

    }


    @Transactional
    @Override
    public void proceedPayment(Long paymentId) {
        Payment payment = findById(paymentId);

        orderService.proceedPayment(payment.getOrder(), payment.getUser());

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
    public Payment findById(Long paymentId) {
        Payment paymentOrder = paymentOrderRepository.findById(paymentId).orElseThrow(() -> new AppException("Payment not found"));
        return paymentOrder;
    }





    @Override
    public List<Payment> findUserPaymentOrdersPaymentNotYet(User user) {
        return paymentOrderRepository.findUserPaymentOrdersPaymentNotYet(
                PAYMENT_ORDER_STATUS.PENDING, user.getId()
        );
    }


}
