package com.vanrin05.app.controller;

import com.vanrin05.app.model.*;
import com.vanrin05.app.service.PaymentService;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.TransactionService;
import com.vanrin05.app.service.impl.SellerService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    UserService userService;
    SellerService sellerService;
    SellerReportService sellerReportService;
    TransactionService transactionService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentOrder> findPaymentById(@PathVariable("paymentId") Long paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }

    @GetMapping("/{paymentId}/orders")
    public ResponseEntity<List<Order>> findPaymentHasOrders(@PathVariable("paymentId") Long paymentId) {
        return ResponseEntity.ok(paymentService.findAllOrdersInPaymentOrder(paymentService.findById(paymentId)));
    }
    @GetMapping
    public ResponseEntity<List<PaymentOrder>> findAllPayments(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(paymentService.findUserPaymentOrders(user));
    }

}
