package com.vanrin05.controller;


import com.vanrin05.domain.PAYMENT_METHOD;
import com.vanrin05.dto.response.PaymentLinkResponse;
import com.vanrin05.model.*;
import com.vanrin05.service.CartService;
import com.vanrin05.service.OrderService;
import com.vanrin05.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    UserService userService;
    CartService cartService;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(
            @RequestBody Address shippingAddress,
            @RequestParam PAYMENT_METHOD paymentMethod,
            @RequestHeader("Authorization") String jwt
            ){
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrders(user, shippingAddress, cart);

        return ResponseEntity.ok(new PaymentLinkResponse());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrdersHistory(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(orderService.userOrdersHistory(user.getId()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") Long orderId) {
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable("orderItemId") Long orderItemId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItem);
    }
}
