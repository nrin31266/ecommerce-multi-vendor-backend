package com.vanrin05.utils;

import com.vanrin05.exception.AppException;
import com.vanrin05.repository.httpclient.VNPayClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {
    VNPayClient vnPayClient;

    @NonFinal
    @Value("${vnpay.tmn-code}")
    String vnp_TmnCode;
    @NonFinal
    @Value("${vnpay.hash-secret}")
    String vnp_HashSecret;
    @NonFinal
    @Value("${vnpay.url}")
    String vnp_Url;
    @NonFinal
    @Value("${vnpay.return-url}")
    String vnp_ReturnUrl;


    private final String vnp_Version = "2.1.0";

    private final String vnp_Command = "pay";

    public String createPaymentUrl(long amount, String orderInfo, String orderType, String bankCode) {
        log.info(vnp_HashSecret);
        log.info(vnp_TmnCode);
        try {
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", vnp_Version);
            params.put("vnp_Command", vnp_Command);
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
            params.put("vnp_TxnRef", System.currentTimeMillis() + "_" + UUID.randomUUID());

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
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    if(itr.hasNext()) {
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }

            log.warn(hashData.toString());

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

            return vnp_Url + "?" + queryUrl + "&vnp_SecureHash=" + vnp_SecureHash;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object queryTransaction(String orderId, Date transDate){
        String vnp_RequestId =  System.currentTimeMillis() + "_" + UUID.randomUUID();
        String vnp_Command = "querydr";
        String vnp_TxnRef = orderId;
        String vnp_OrderInfo = "Check GD OrderId:" + vnp_TxnRef;
        //String vnp_TransactionNo = req.getParameter("transactionNo");
        String vnp_TransDate = DateUtil.formatDate(transDate, "yyyyMMddHHmmss");
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String vnp_IpAddr = "127.0.0.1";
        JSONObject vnp_Params = new JSONObject();
        vnp_Params.put("vnp_RequestId", vnp_RequestId);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        //vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.put("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hash_Data);
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        var res = vnPayClient.querydr(vnp_Params.toString());
        return res;
    }

    public void handlerInp(Map<String, String> params) {
        boolean isChecksum = checksum(params);
        log.info("Checksum: {}", isChecksum);
        if (!isChecksum){
            throw new AppException("Checksum vnpay is false");
        }
    }

    private boolean checksum(Map<String, String> params) {
        String receivedSecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = params.get(fieldName);
            if(fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if(itr.hasNext()) {
                    hashData.append("&");
                }
            }
        }
        return receivedSecureHash.equals(hmacSHA512(vnp_HashSecret, hashData.toString()));
    }


    private static String hmacSHA512(final String key, final String data) {
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
