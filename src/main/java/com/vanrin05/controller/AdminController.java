package com.vanrin05.controller;

import com.vanrin05.domain.ACCOUNT_STATUS;
import com.vanrin05.model.Seller;
import com.vanrin05.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    SellerService sellerService;

    @PutMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable ACCOUNT_STATUS status
            ){
        Seller updateSeller = sellerService.updateSellerAccountStatus(id, status);

        return ResponseEntity.ok(updateSeller);
    }
}
