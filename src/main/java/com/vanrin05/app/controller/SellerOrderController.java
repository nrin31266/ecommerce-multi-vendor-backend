package com.vanrin05.app.controller;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.exception.AppException;
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



    @GetMapping("/{orderItemStatus}")
    public ResponseEntity<List<OrderItem>> getAllOrders(@RequestHeader("Authorization") String jwt, @PathVariable("orderItemStatus") ORDER_ITEM_STATUS orderItemStatus) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(orderService.sellerOrders(seller, orderItemStatus));
    }


    @PutMapping("/{orderId}/item/{orderItemId}/status/{status}")
    public ResponseEntity<OrderItem> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                       @PathVariable("orderItemId") Long orderItemId,
                                                       @PathVariable("status")ORDER_ITEM_STATUS status,
                                                       @RequestHeader("Authorization") String jwt
    ){

        if(status == ORDER_ITEM_STATUS.COMPLETED || status == ORDER_ITEM_STATUS.PENDING
        || status == ORDER_ITEM_STATUS.CANCELLED){
            throw new AppException("Order status invalid");
        }

        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(orderService.updateOrderItemStatus(orderId, status, orderItemId, seller));
    }


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
