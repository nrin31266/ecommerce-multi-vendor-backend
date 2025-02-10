package com.vanrin05.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
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

    public String createPaymentUrl(long amount, String orderInfo, String orderType, String bankCode) {


        try {
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf(amount * 100));
            if (bankCode != null && !bankCode.isEmpty()) {
                params.put("vnp_BankCode", bankCode);
            }
            params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_IpAddr", "127.0.0.1");
            params.put("vnp_Locale", "vn");
            params.put("vnp_OrderInfo", orderInfo);
            params.put("vnp_OrderType", orderType);
            params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            params.put("vnp_ExpireDate", new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date(Instant.now().plus(15, ChronoUnit.MINUTES)
                            .toEpochMilli())));
            params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));

            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = params.get(fieldName);
                if(fieldValue != null && !fieldValue.isEmpty()) {
                    // Build hash data
                    hashData.append(fieldName).append("=");
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if(itr.hasNext()) {
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

            return vnp_Url + "?" + queryUrl + "&vnp_SecureHash=" + vnp_SecureHash;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }


}
