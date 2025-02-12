package com.vanrin05.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;

import java.util.Map;
import java.util.Set;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long paymentOrderId);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
    String createVNPaymentLink(User user, Long amount, Long orderId, Map<String, String> params);
    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
