package com.vanrin05.app.controller;

import com.vanrin05.app.dto.response.VNPayInpResponse;
import com.vanrin05.app.service.PaymentService;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.TransactionService;
import com.vanrin05.app.service.impl.SellerService;
import com.vanrin05.app.service.impl.UserService;
import com.vanrin05.app.service.impl.VNPayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {
    VNPayService vnPayService;
    PaymentService paymentService;
    UserService userService;
    SellerService sellerService;
    SellerReportService sellerReportService;
    TransactionService transactionService;



    @GetMapping("/pay")
    public String pay(@RequestParam("amount") long amount,
                      @RequestParam("orderInfo") String orderInfo,
                      @RequestParam("orderType") String orderType,
                      @RequestParam(value = "bankCode", required = false) String bankCode) {
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode, null);
    }

    @GetMapping("/return")
    public ResponseEntity<Void> returnPayment(@RequestParam Map<String, String> params) {
        String paymentId = params.get("vnp_TxnRef");
        String redirectUrl = "http://localhost:5173/payment-success/" +paymentId ;
        
        if (!"00".equals(params.get("vnp_ResponseCode"))) {
            redirectUrl = "http://localhost:5173/payment-cancel/" +paymentId ;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
    }


    @GetMapping("/ipn")
    public ResponseEntity<VNPayInpResponse> ipn(@RequestParam Map<String, String> params) {
        String paymentId = params.get("vnp_TxnRef");
        String vnp_ResponseCode = params.get("vnp_ResponseCode");

        VNPayInpResponse res = new VNPayInpResponse();

        if (paymentId == null || !paymentId.matches("\\d+")) {
            res.setRspCode("97"); // Transaction Not Found
            res.setMessage("Invalid Payment ID");
            return ResponseEntity.ok(res);
        }

        try {
            vnPayService.handlerInp(params);

            if ("00".equals(vnp_ResponseCode)) {
                paymentService.proceedPayment(Long.valueOf(paymentId));
                res.setRspCode("00"); // Success
                res.setMessage("Payment successfully processed.");
            } else {
                res.setRspCode(vnp_ResponseCode);
                res.setMessage("Payment failed with VNPAY Response Code: " + vnp_ResponseCode);
            }
        } catch (Exception e) {
            res.setRspCode("99"); // Unknown Error
            res.setMessage("Internal Server Error: " + e.getMessage());
        }

        return ResponseEntity.ok(res);
    }


    @GetMapping("/querydr")
    public ResponseEntity<Object> querydr(@RequestParam String orderId,
                                          @RequestParam("transDate")
                                          @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date transDate) {
        return ResponseEntity.ok(vnPayService.queryTransaction(orderId, transDate));

    }


}
