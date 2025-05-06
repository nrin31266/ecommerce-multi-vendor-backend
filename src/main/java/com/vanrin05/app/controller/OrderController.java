package com.vanrin05.app.controller;


import com.stripe.exception.StripeException;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.dto.response.PaymentResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.Payment;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import com.vanrin05.app.service.*;
import com.vanrin05.app.service.impl.SellerService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    UserService userService;
    CartService cartService;
    SellerService sellerService;
    SellerReportService sellerReportService;
    PaymentService paymentService;
    AddressService addressService;


    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestBody CreateOrderRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws StripeException {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        // Create orders
        Order order = orderService.createOrder(user, addressService.getAddressById(request.getAddressId()), cart, request);

        if(request.getPaymentMethod().equals(PAYMENT_METHOD.CASH_ON_DELIVERY)){
            return ResponseEntity.ok(new PaymentResponse());
        }
        Payment payment = paymentService.createPaymentOrder(user, order, request.getPaymentMethod());
        if(request.getPaymentMethod().equals(PAYMENT_METHOD.VNPAY)){
            Map<String, String> params = new HashMap<>();
            params.put("orderInfo", "Payment order with id: " + payment.getId());
            params.put("orderType", "other");
//            params.put("bankCode", null);
            String linkUrl = paymentService.createVNPaymentLink(user, payment.getAmount(), payment.getId(), params);

            return ResponseEntity.ok(new PaymentResponse(linkUrl));
        }else if(request.getPaymentMethod().equals(PAYMENT_METHOD.STRIPE)){
            String linkUrl = paymentService.createStripePaymentLink(user, payment.getAmount() ,payment.getId());
            return ResponseEntity.ok(new PaymentResponse(linkUrl));
        }else{
            throw new AppException("Payment method not supported");
        }

    }

//    @GetMapping("/user")
//    public ResponseEntity<List<Order>> getUserOrdersHistory(@RequestHeader("Authorization") String jwt) {
//        User user = userService.findUserByJwtToken(jwt);
//        return ResponseEntity.ok(orderService.userOrdersHistory(user.getId()));
//    }

    @GetMapping("/user")
    public ResponseEntity<List<SellerOrder>> getUserOrdersHistory(@RequestHeader("Authorization") String jwt,
                                                            @RequestParam("status") SELLER_ORDER_STATUS status) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(orderService.userOrders(user, status));
    }

    @GetMapping("/user/{sellerOrderId}")
    public ResponseEntity<SellerOrder> getSellerOrderDetails(@RequestHeader("Authorization") String jwt,
                                                                   @PathVariable("sellerOrderId") Long sellerOrderId) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(orderService.getSellerOrderById(sellerOrderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") Long orderId) {
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable("orderItemId") Long orderItemId, @RequestHeader("Authorization") String jwt) {
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItem);
    }

    @GetMapping("/seller-order/{sellerOrderId}")
    public ResponseEntity<SellerOrder> getSellerOrderById(@PathVariable("sellerOrderId") Long sellerOrderId, @RequestHeader("Authorization") String jwt) {
        SellerOrder sellerOrder = orderService.getSellerOrderById(sellerOrderId);
        return ResponseEntity.ok(sellerOrder);
    }




}
