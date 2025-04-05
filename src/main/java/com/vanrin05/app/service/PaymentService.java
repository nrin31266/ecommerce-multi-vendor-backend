package com.vanrin05.app.service;

import com.stripe.exception.StripeException;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.PaymentOrder;
import com.vanrin05.app.model.User;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, List<Order> orders,  PAYMENT_METHOD paymentMethod);
    Boolean proceedPayment(Long paymentId);
    String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params);
    String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException;

    PaymentOrder findById(Long paymentId);
    List<Order> findAllOrdersInPaymentOrder(PaymentOrder paymentOrder);
    List<PaymentOrder> findUserPaymentOrders(User user);

    PaymentOrder cancelPaymentOrder(Long paymentId, User user);
}
