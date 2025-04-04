package com.vanrin05.app.repository;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.model.PaymentOrder;
import com.vanrin05.app.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    @Query("SELECT p_o FROM payment_orders p_o LEFT JOIN orders o WHERE p_o.id=:id")
    Optional<PaymentOrder> customFindByIdWithOrders(@Param("id") Long id);

    List<PaymentOrder> findByUser(User user);

    @Query("SELECT p_o FROM payment_orders p_o LEFT JOIN orders o WHERE p_o.paymentOrderStatus =:paymentOrderStatus AND p_o.paymentExpiry <= :now")
    List<PaymentOrder> customFindByExpiredOnlinePayment(@Param("paymentOrderStatus") PAYMENT_ORDER_STATUS paymentOrderStatus, @Param("now") LocalDateTime now);

    @EntityGraph(attributePaths = {"orders.orderItems.product"}) // Fetch all in one query
    @Query("SELECT po FROM payment_orders po " +
           "WHERE po.paymentOrderStatus = :status " +
           "AND po.paymentExpiry <= :now")
    List<PaymentOrder> findByStatusAndPaymentExpiryBeforeWithOrders(
            @Param("status") PAYMENT_ORDER_STATUS status,
            @Param("now") LocalDateTime now
    );
}
