package com.vanrin05.app.repository;

import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.model.orderpayment.Payment;
import com.vanrin05.app.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByUser(User user);

    @Query("SELECT p FROM payments p WHERE p.paymentOrderStatus =:paymentOrderStatus AND p.expiryDate <:now")
    List<Payment> findExpiredPaymentsByStatus(
            @Param("paymentOrderStatus") PAYMENT_ORDER_STATUS paymentOrderStatus,
            @Param("now") LocalDateTime now
    );

    @Query("SELECT p FROM payments p WHERE p.paymentOrderStatus =:paymentOrderStatus AND p.user.id=:userId")
    List<Payment> findUserPaymentOrdersPaymentNotYet(@Param("paymentOrderStatus") PAYMENT_ORDER_STATUS paymentOrderStatus,@Param("userId") Long userId);

}
