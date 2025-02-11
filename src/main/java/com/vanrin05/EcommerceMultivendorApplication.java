package com.vanrin05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EcommerceMultivendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMultivendorApplication.class, args);
	}

}
