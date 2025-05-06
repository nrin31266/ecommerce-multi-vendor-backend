package com.vanrin05.app.repository;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long> {
    List<SellerOrder> findBySellerAndStatus(Seller seller, SELLER_ORDER_STATUS status);
    List<SellerOrder> findByUserIdAndStatus(Long userId, SELLER_ORDER_STATUS status);
}
