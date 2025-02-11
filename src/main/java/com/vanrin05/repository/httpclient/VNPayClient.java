package com.vanrin05.repository.httpclient;

import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "https://sandbox.vnpayment.vn", name = "VNPayClient")
public interface VNPayClient {
    @PostMapping(value = "/merchant_webapi/api/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object querydr(@RequestBody Object request);
}
