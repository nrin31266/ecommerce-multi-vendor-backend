package com.vanrin05.controller;

import com.vanrin05.model.Seller;
import com.vanrin05.model.Transaction;
import com.vanrin05.service.TransactionService;
import com.vanrin05.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;
    SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsBySeller(
            @RequestHeader("Authorization") String jwtToken
    ) {
        Seller seller = sellerService.getSellerProfile(jwtToken);

        return ResponseEntity.ok(transactionService.getAllTransactionsBySeller(seller));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }


}
