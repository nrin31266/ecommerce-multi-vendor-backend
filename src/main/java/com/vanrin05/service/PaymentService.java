package com.vanrin05.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.vanrin05.domain.PAYMENT_METHOD;
import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;

import java.util.Map;
import java.util.Set;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, Set<Order> orders,  PAYMENT_METHOD paymentMethod);
    Boolean proceedPayment(Long paymentId);
    String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params);
    String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException;
}
