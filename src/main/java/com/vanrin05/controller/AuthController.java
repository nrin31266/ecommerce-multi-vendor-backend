package com.vanrin05.controller;

import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.model.User;
import com.vanrin05.service.AuthService;
import jakarta.mail.MessagingException;
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
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) {
        return ResponseEntity.ok(authService.signup(req));

    }

    @PostMapping("/send-login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestParam("email") String email) throws MessagingException {
        authService.sendLoginOtp(email);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Sent otp successfully")
                .build());

    }
}
