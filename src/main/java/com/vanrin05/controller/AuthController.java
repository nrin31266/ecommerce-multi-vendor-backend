package com.vanrin05.controller;

import com.vanrin05.domain.ACCOUNT_STATUS;
import com.vanrin05.dto.request.SigningOtpRequest;
import com.vanrin05.dto.request.SigningRequest;
import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.model.Seller;
import com.vanrin05.model.User;
import com.vanrin05.service.AuthService;
import com.vanrin05.service.SellerService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;
    private final SellerService sellerService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) {
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

//    @PutMapping("/verify/{otp}")
//    public ResponseEntity<AuthResponse> verifySellerEmail(@PathVariable String otp, ) {}

    @GetMapping("/profile")
    public ResponseEntity<Seller> getProfileHandler(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(sellerService.getSellerProfile(jwt));
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellerHandler(@RequestParam ACCOUNT_STATUS status) {
        return ResponseEntity.ok(sellerService.getAllSellers(status));
    }
}
