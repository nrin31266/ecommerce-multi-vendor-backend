package com.vanrin05.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    public String generateOtp(int length) {

        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }
}
