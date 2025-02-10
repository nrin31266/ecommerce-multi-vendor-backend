package com.vanrin05.controller;

import com.vanrin05.service.impl.VNPayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode);
    }

    @GetMapping("/return")
    public String returnPayment(@RequestParam Map<String, String> params) {
        // Xử lý khi người dùng quay lại website sau khi thanh toán
        if ("00".equals(params.get("vnp_ResponseCode"))) {
            return "Thanh toán thành công!";
        } else {
            return "Thanh toán thất bại!";
        }
    }
    @PostMapping("/ipn")
    public String ipn(@RequestParam Map<String, String> params) {
        // Xử lý IPN callback (server call server)
        if ("00".equals(params.get("vnp_ResponseCode"))) {
            return "Giao dịch thành công!";
        } else {
            return "Giao dịch thất bại!";
        }
    }

}
