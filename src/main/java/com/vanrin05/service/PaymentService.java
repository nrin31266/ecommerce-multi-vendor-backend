package com.vanrin05.service;

import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long paymentOrderId);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId);
    PaymentLinkResponse createPaymentLink(User user, Long amount, String orderInfo, String orderType);
    String create();
}
