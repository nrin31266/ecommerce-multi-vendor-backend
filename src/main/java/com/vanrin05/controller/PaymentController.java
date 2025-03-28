package com.vanrin05.controller;

import com.razorpay.RazorpayException;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.model.*;
import com.vanrin05.service.PaymentService;
import com.vanrin05.service.SellerReportService;
import com.vanrin05.service.TransactionService;
import com.vanrin05.service.impl.SellerService;
import com.vanrin05.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
