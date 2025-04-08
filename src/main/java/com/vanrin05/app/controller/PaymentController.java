package com.vanrin05.app.controller;

import com.vanrin05.app.model.*;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.Payment;
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


    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> findPaymentById(@PathVariable("paymentId") Long paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }

    @GetMapping("/pending-payment")
    public ResponseEntity<List<Payment>> findUserPaymentOrdersPaymentNotYet(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(paymentService.findUserPaymentOrdersPaymentNotYet(user));
    }


    @PutMapping("/cancel/{paymentId}")
    public ResponseEntity<Payment> cancelPayment(@PathVariable("paymentId") Long paymentId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Payment payment = paymentService.findById(paymentId);
        return ResponseEntity.ok(paymentService.cancelPaymentOrder(payment, user, "Cancel payment"));
    }
}
