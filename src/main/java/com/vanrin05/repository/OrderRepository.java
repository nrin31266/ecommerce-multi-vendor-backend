package com.vanrin05.repository;

import com.vanrin05.model.Order;
import com.vanrin05.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findAllByPaymentOrder(PaymentOrder paymentOrder);
}
