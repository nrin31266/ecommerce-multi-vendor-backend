package com.vanrin05.app.service;

import com.stripe.exception.StripeException;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.Payment;

import com.vanrin05.app.model.User;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    Payment createPaymentOrder(User user, Order order, PAYMENT_METHOD paymentMethod) throws StripeException;
    void proceedPayment(Long paymentId);
    String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params);
    String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException;

    Payment findById(Long paymentId);

    List<Payment> findUserPaymentOrdersPaymentNotYet(User user);

    Payment cancelPaymentOrder(Payment payment, User user, String cancelReason);
}
