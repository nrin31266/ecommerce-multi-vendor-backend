package com.vanrin05.app.controller;

import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.Seller;
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


//    @PutMapping("/{orderId}/status/{orderStatus}")
//    public ResponseEntity<Order> updateOrderStatus(@PathVariable("orderId") long orderId, @PathVariable("orderStatus") ORDER_STATUS orderStatus) {
//        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));
//    }
//
//    @GetMapping("/{orderStatus}")
//    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String jwt, @PathVariable("orderStatus") ORDER_STATUS orderStatus) {
//        Seller seller = sellerService.getSellerProfile(jwt);
//        return ResponseEntity.ok(orderService.sellerOrders(seller.getId(), orderStatus));
//
//    }
}
