package com.vanrin05.controller;

import com.vanrin05.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse homeControllerHandler() {
        return ApiResponse.builder()
                .message("Hello world!")
                .build();
    }
}
