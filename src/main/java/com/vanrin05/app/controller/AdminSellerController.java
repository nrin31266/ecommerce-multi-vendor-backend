package com.vanrin05.app.controller;

import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/sellers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminSellerController {
    SellerService sellerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{sellerId}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long sellerId,
            @PathVariable ACCOUNT_STATUS status
            ){
        Seller updateSeller = sellerService.updateSellerAccountStatus(sellerId, status);

        return ResponseEntity.ok(updateSeller);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellersAcceptTerms(){
        return ResponseEntity.ok(sellerService.getAllSellers());
    }
}
