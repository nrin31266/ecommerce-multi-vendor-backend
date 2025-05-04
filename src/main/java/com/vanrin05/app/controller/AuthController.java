package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.SigningOtpRequest;
import com.vanrin05.app.dto.request.SigningRequest;
import com.vanrin05.app.dto.request.SignupRequest;
import com.vanrin05.app.dto.request.VerifyTokenRequest;
import com.vanrin05.app.dto.response.ApiResponse;
import com.vanrin05.app.dto.response.AuthResponse;
import com.vanrin05.app.dto.response.VerifyTokenResponse;
import com.vanrin05.app.service.impl.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public ResponseEntity<ApiResponse> sendOtpHandler(@Valid @RequestBody SigningOtpRequest request) throws MessagingException {
//        log.info(request.toString());
//        authService.sendLoginOtp(request.getEmail(), request.getRole());
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Sent otp successfully")
                .build());
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signingHandler(@RequestBody SigningRequest req) {

        return ResponseEntity.ok(authService.signing(req));

    }

    @PostMapping("/valid-token")
    public ResponseEntity<VerifyTokenResponse> verifyToken(
            @RequestBody @Valid VerifyTokenRequest req
    ){
        return ResponseEntity.ok(authService.verifyToken(req.getJwtToken()));
    }



}
