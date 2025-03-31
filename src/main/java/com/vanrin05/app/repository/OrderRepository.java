package com.vanrin05.app.repository;

import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.model.Order;
import com.vanrin05.app.model.PaymentOrder;
import com.vanrin05.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByUserId(Long userId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findAllByPaymentOrder(PaymentOrder paymentOrder);
    List<Order> findAllByUserId(Long id);
    @Query("SELECT o FROM orders o WHERE o.user.id = :userId AND o.paymentDetails.paymentStatus = :paymentStatus")
    List<Order> customFindByUserId(@Param("userId") Long userId, @Param("paymentStatus") PAYMENT_STATUS paymentStatus);
    List<Order> findByUserIdAndPaymentDetails_PaymentStatus(Long userId, PAYMENT_STATUS paymentStatus);

}
