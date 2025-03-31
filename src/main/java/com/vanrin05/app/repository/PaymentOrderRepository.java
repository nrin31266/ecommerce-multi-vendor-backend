package com.vanrin05.app.repository;

import com.vanrin05.app.model.PaymentOrder;
import com.vanrin05.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    @Query("SELECT p_o FROM payment_orders p_o LEFT JOIN orders o WHERE p_o.id=:id")
    Optional<PaymentOrder> customFindByIdWithOrders(@Param("id") Long id);

    List<PaymentOrder> findByUser(User user);
}
