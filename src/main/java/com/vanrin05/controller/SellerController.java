package com.vanrin05.controller;

import com.vanrin05.dto.request.CreateSellerRequest;
import com.vanrin05.dto.request.SigningRequest;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.model.Seller;
import com.vanrin05.service.AuthService;
import com.vanrin05.service.SellerService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/sellers")
public class SellerController {
    SellerService sellerService;
    AuthService authService;
    String SELLER_PREFIX = "seller_";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody SigningRequest requestBody
    ) {
        requestBody.setEmail(SELLER_PREFIX + requestBody.getEmail());

        return ResponseEntity.ok(authService.signing(requestBody));
    }

    @PostMapping
    public ResponseEntity<Seller> registerHandler(@RequestBody CreateSellerRequest requestBody) throws MessagingException {
        return ResponseEntity.ok(sellerService.createSeller(requestBody));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sellerService.getSellerById(id));
    }
}
