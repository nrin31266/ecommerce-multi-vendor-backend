package com.vanrin05.controller;

import com.vanrin05.dto.response.VNPayInpResponse;
import com.vanrin05.service.impl.VNPayService;
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

    @GetMapping("/pay")
    public String pay(@RequestParam("amount") long amount,
                      @RequestParam("orderInfo") String orderInfo,
                      @RequestParam("orderType") String orderType,
                      @RequestParam(value = "bankCode", required = false) String bankCode) {
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode, null);
    }

    @GetMapping("/return")
    public ResponseEntity<Void> returnPayment(@RequestParam Map<String, String> params) {
        String orderId = params.get("vnp_TxnRef");
        String redirectUrl = "http://localhost:5173/payment-success/" +orderId ;

        if (!"00".equals(params.get("vnp_ResponseCode"))) {
            redirectUrl = "http://localhost:5173/payment-cancel/" +orderId ;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
    }


    @GetMapping("/ipn")
    public ResponseEntity<VNPayInpResponse> ipn(@RequestParam Map<String, String> params) {
        String orderId = params.get("vnp_TxnRef");
        vnPayService.handlerInp(params);

        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        VNPayInpResponse res = VNPayInpResponse.builder()
                .RspCode(vnp_ResponseCode)
                .Message(vnp_ResponseCode.equals("00") ? "Success" : "Fail")
                .build();
        log.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/querydr")
    public ResponseEntity<Object> querydr(@RequestParam String orderId,
                                          @RequestParam("transDate")
                                          @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") Date transDate) {
        return ResponseEntity.ok(vnPayService.queryTransaction(orderId, transDate));

    }


}
