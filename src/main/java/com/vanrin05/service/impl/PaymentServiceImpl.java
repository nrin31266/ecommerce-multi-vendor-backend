package com.vanrin05.service.impl;

import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.exception.AppException;
import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import com.vanrin05.model.User;
import com.vanrin05.repository.OrderRepository;
import com.vanrin05.repository.PaymentOrderRepository;
import com.vanrin05.service.PaymentService;
import com.vanrin05.utils.VNPayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Set;
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentOrderRepository paymentOrderRepository;
    OrderRepository orderRepository;
    VNPayService vnPayService;


    @Override
    public PaymentOrder createPaymentOrder(User user, Set<Order> orders) {
        Long amount =  orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

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
        PaymentOrder paymentOrder= paymentOrderRepository.findByPaymentLinkId(paymentId);
        if (paymentOrder == null) {
            throw new AppException("Payment not found with payment link id: " + paymentId);
        }

        return paymentOrder;
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        return null;
    }

    @Override
    public PaymentLinkResponse createPaymentLink(User user, Long amount, String orderInfo, String orderType) {
        return null;
    }

    @Override
    public String create() {
        return "";
    }
}
