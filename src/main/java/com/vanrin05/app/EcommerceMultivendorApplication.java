package com.vanrin05.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling //scheduled tasks
public class EcommerceMultivendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMultivendorApplication.class, args);
	}

}
