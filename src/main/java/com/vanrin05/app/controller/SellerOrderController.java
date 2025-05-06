package com.vanrin05.app.controller;

import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.dto.response.UserOrderHistoryResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/seller/orders")
public class SellerOrderController {
    OrderService orderService;
    SellerService sellerService;



    @GetMapping
    public ResponseEntity<List<UserOrderHistoryResponse>> getAllSellerOrders(@RequestHeader("Authorization") String jwt,
                                                                             @RequestParam("status") SELLER_ORDER_STATUS status) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(orderService.sellerOrders(seller, status));
    }


    @PutMapping("/seller-order/{sellerOrderId}/status/{status}")
    public ResponseEntity<SellerOrder> updateOrderStatus(@PathVariable("sellerOrderId") Long sellerOrderId,
                                                       @PathVariable("status")SELLER_ORDER_STATUS status,
                                                       @RequestHeader("Authorization") String jwt
    ){

        if(status == SELLER_ORDER_STATUS.COMPLETED || status == SELLER_ORDER_STATUS.PENDING
        || status == SELLER_ORDER_STATUS.CANCELLED || status == SELLER_ORDER_STATUS.REFUNDED
        || status == SELLER_ORDER_STATUS.PENDING_PAYMENT){
            throw new AppException("Order status invalid, you can't update the status for this order");
        }

        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(orderService.updateSellerOrderStatus(status, sellerOrderId, seller));
    }


    @PutMapping("/seller-order/reject/{sellerOrderId}")
    public ResponseEntity<SellerOrder> cancelOrder(@RequestHeader("Authorization") String jwt,
                                             @PathVariable("sellerOrderId") Long sellerOrderId) {

        Seller seller = sellerService.getSellerProfile(jwt);

        return ResponseEntity.ok(orderService.sellerCancelOrder(seller, "Seller cancel", sellerOrderId));
    }

    @PutMapping("/seller-order/approve/{sellerOrderId}")
    public ResponseEntity<SellerOrder> approveOrder(@PathVariable("sellerOrderId") Long sellerOrderId, @RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(orderService.approveSellerOrder(sellerOrderId, seller));
    }
}
