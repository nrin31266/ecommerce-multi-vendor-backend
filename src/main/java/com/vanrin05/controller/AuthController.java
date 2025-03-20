package com.vanrin05.controller;

import com.vanrin05.dto.request.SigningOtpRequest;
import com.vanrin05.dto.request.SigningRequest;
import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.service.impl.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody @Valid SignupRequest req) {
        return ResponseEntity.ok(authService.signup(req));

    }


    @PostMapping("/send-login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody SigningOtpRequest request) throws MessagingException {
        authService.sendLoginOtp(request.getEmail(), request.getRole());
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Sent otp successfully")
                .build());
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signingHandler(@RequestBody SigningRequest req) {
        return ResponseEntity.ok(authService.signing(req));

    }



}
