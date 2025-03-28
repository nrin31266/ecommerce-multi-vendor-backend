package com.vanrin05.service;

import com.stripe.exception.StripeException;
import com.vanrin05.domain.PAYMENT_METHOD;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, List<Order> orders,  PAYMENT_METHOD paymentMethod);
    Boolean proceedPayment(Long paymentId);
    String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params);
    String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException;

    PaymentOrder findById(Long paymentId);
    List<Order> findAllOrdersInPaymentOrder(PaymentOrder paymentOrder);
    List<PaymentOrder> findUserPaymentOrders(User user);
}
