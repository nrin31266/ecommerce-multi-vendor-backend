package com.vanrin05.app.repository;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long> {
    List<SellerOrder> findBySellerAndStatus(Seller seller, SELLER_ORDER_STATUS status);
    List<SellerOrder> findByUserIdAndStatus(Long userId, SELLER_ORDER_STATUS status);
    @Query("SELECT so FROM SellerOrder so " +
           "WHERE so.status = :status " +
           "AND so.deliveredDate <= :dateLimit")
    List<SellerOrder> findDeliveredOrdersOlderThan(
            @Param("status") SELLER_ORDER_STATUS status,
            @Param("dateLimit") LocalDateTime dateLimit
    );

}
