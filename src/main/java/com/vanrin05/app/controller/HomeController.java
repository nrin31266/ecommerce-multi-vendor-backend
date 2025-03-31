package com.vanrin05.app.controller;

import com.vanrin05.app.dto.response.ApiResponse;
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
