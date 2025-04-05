package com.vanrin05.app.controller;


import com.stripe.exception.StripeException;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.dto.response.PaymentResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.PaymentOrder;
import com.vanrin05.app.service.CartService;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.PaymentService;
import com.vanrin05.app.service.SellerReportService;
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


    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestBody Address shippingAddress,
            @RequestParam PAYMENT_METHOD paymentMethod,
            @RequestHeader("Authorization") String jwt
            ) throws StripeException {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        PaymentResponse paymentResponse = new PaymentResponse();

        // Create orders
        List<Order> orders = orderService.createOrders(user, shippingAddress, cart, paymentMethod);

        if(paymentMethod.equals(PAYMENT_METHOD.CASH_ON_DELIVERY)){

            return ResponseEntity.ok(paymentResponse);
        }
        //Create payment
        PaymentOrder paymentOrder = paymentService.createPaymentOrder(user, orders, paymentMethod);




        if(paymentMethod.equals(PAYMENT_METHOD.VNPAY)){
            Map<String, String> params = new HashMap<>();
            params.put("orderInfo", "Payment order with id: " + paymentOrder.getId());
            params.put("orderType", "other");
//            params.put("bankCode", null);
            String linkUrl = paymentService.createVNPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId(), params);

            paymentResponse.setPayment_link_url(linkUrl);

        }else if(paymentMethod.equals(PAYMENT_METHOD.STRIPE)){
            String linkUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount() ,paymentOrder.getId());
            paymentResponse.setPayment_link_url(linkUrl);
        }else{
            throw new AppException("Invalid payment method: " + paymentMethod);
        }


        return ResponseEntity.ok(paymentResponse);
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
        log.info("OrderItemId: {}", orderItemId);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        return ResponseEntity.ok(orderItem);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        sellerReport.setCanceledOrders(sellerReport.getCanceledOrders() + 1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);


        return ResponseEntity.ok(orderService.cancelOrder(orderId, user));
    }


}
