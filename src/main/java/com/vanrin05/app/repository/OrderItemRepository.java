package com.vanrin05.app.repository;

import com.vanrin05.app.model.orderpayment.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
