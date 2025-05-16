package com.vanrin05.app.controller;

import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.dto.request.CreateSellerRequest;
import com.vanrin05.app.dto.request.SigningRequest;
import com.vanrin05.app.dto.request.UpdateSellerRequest;
import com.vanrin05.app.dto.response.AuthResponse;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.impl.AuthService;
import com.vanrin05.app.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/sellers")
public class SellerController {
    SellerService sellerService;
    AuthService authService;
    SellerReportService sellerReportService;
    String SELLER_PREFIX = "seller_";



    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody SigningRequest requestBody
    ) {
        requestBody.setEmail(SELLER_PREFIX + requestBody.getEmail());

        return ResponseEntity.ok(authService.signing(requestBody));
    }

    @PostMapping
    public ResponseEntity<Seller> registerHandler(@RequestBody CreateSellerRequest requestBody)  {
        return ResponseEntity.ok(sellerService.createSeller(requestBody));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sellerService.getSellerById(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getProfileHandler(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(sellerService.getSellerProfile(jwt));
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellerHandler(@RequestParam ACCOUNT_STATUS status) {
        return ResponseEntity.ok(sellerService.getAllSellers(status));
    }

    @PutMapping("/profile")
    public ResponseEntity<Seller> updateSellerHandler(@RequestHeader("Authorization") String jwt, @RequestBody UpdateSellerRequest req) {
        return ResponseEntity.ok(sellerService.updateSeller(jwt, req));
    }

    @PutMapping("/accept-terms")
    public ResponseEntity<Seller> verifySellerEmail(@RequestHeader("Authorization") String jwt ) {
        return ResponseEntity.ok(sellerService.accessTerms(jwt));
    }


    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Void> deleteSellerHandler(@PathVariable("sellerId") Long sellerId) {
        sellerService.deleteSeller(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/report/{sellerId}")
    public ResponseEntity<SellerReport> getSellerReportHandler(
            @PathVariable Long sellerId
    ) {
        return ResponseEntity.ok(sellerReportService.getSellerReport(sellerService.getSellerById(sellerId)));
    }
}
