package com.vanrin05.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Service
public class VNPayService {
    @Value("${vnpay.tmn-code}")
    private String vnp_TmnCode;

    @Value("${vnpay.hash-secret}")
    private String vnp_HashSecret;

    @Value("${vnpay.url}")
    private String vnp_Url;

    @Value("${vnpay.return-url}")
    private String vnp_ReturnUrl;

    @Value("${vnpay.ipn-url}")
    private String vnp_IpnUrl;

    public String createPaymentUrl(long amount, String orderInfo) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay tính theo VND * 100
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            params.put("vnp_OrderInfo", orderInfo);
            params.put("vnp_OrderType", "billpayment");
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            params.put("vnp_IpAddr", "127.0.0.1"); // Địa chỉ IP khách hàng
            params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            // Sắp xếp tham số theo thứ tự alphabet
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = params.get(fieldName);
                if (value != null && !value.isEmpty()) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                            .append("&");
                    hashData.append(fieldName).append("=").append(value).append("&");
                }
            }

            // Xóa ký tự `&` cuối cùng
            query.deleteCharAt(query.length() - 1);
            hashData.deleteCharAt(hashData.length() - 1);

            // Tạo chuỗi ký hash
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            return vnp_Url + "?" + query.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String hmacSHA512(String key, String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest((key + data).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing", e);
        }
    }
}
