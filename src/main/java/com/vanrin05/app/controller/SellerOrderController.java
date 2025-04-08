package com.vanrin05.app.controller;

import com.vanrin05.app.model.User;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.impl.SellerService;
import com.vanrin05.app.service.impl.UserService;
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
    UserService userService;


    @PutMapping("/{orderId}/item/{orderItemStatus}/status/{status}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("orderId") long orderId, @PathVariable("orderStatus") ORDER_STATUS orderStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));
    }
//
//    @GetMapping("/{orderStatus}")
//    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String jwt, @PathVariable("orderStatus") ORDER_STATUS orderStatus) {
//        Seller seller = sellerService.getSellerProfile(jwt);
//        return ResponseEntity.ok(orderService.sellerOrders(seller.getId(), orderStatus));
//
//    }

    @PutMapping("/{orderId}/cancel/{orderItemId}")
    public ResponseEntity<OrderItem> cancelOrder(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String jwt,
                                             @PathVariable("orderItemId") Long orderItemId) {

        Order order = orderService.findOrderById(orderId);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        Seller seller = sellerService.getSellerById(orderItem.getSellerId());

        return ResponseEntity.ok(orderService.sellerCancelOrder(order, seller, "Seller cancel", orderItem));
    }

    @PutMapping("/{orderId}/approve/{orderItemId}")
    public ResponseEntity<OrderItem> approveOrder(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String jwt,
                                                 @PathVariable("orderItemId") Long orderItemId) {


        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        Seller seller = sellerService.getSellerById(orderItem.getSellerId());

        return ResponseEntity.ok(orderService.approveOrderItem(orderId, orderItemId, seller));
    }
}
