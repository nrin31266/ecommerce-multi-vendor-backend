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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode, -1L);
    }

    @GetMapping("/return")
    public ResponseEntity<String> returnPayment(@RequestParam Map<String, String> params) {
        String paymentId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        boolean success = "00".equals(responseCode);
        String message = success ? "Payment successfully" : "Payment failed";
        String buttonColor = success ? "#4CAF50" : "#dc3545"; // xanh hoặc đỏ

        String appRedirectUrl = String.format(
                "yourapp://payment-result?method=vnpay&status=%s&paymentId=%s",
                success, paymentId
        );

        String html = """
            <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                    <style>
                        body {
                            font-family: sans-serif;
                            text-align: center;
                            padding-top: 50px;
                            background-color: #f9f9f9;
                        }
                        h2 {
                            color: %s;
                        }
                        button {
                            padding: 12px 24px;
                            font-size: 16px;
                            background-color: %s;
                            color: white;
                            border: none;
                            border-radius: 8px;
                            margin-top: 30px;
                            cursor: pointer;
                        }
                    </style>
                </head>
                <body>
                    <h2>%s</h2>
                    <p>Payment ID: %s</p>
                    <button onclick="window.location.href='%s'">Return to App</button>
                </body>
            </html>
        """.formatted(
                buttonColor, // màu tiêu đề
                buttonColor, // màu nút
                message,
                paymentId,
                appRedirectUrl
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
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
