package com.vanrin05.app.controller;

import com.vanrin05.app.model.SellerReport;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/sellers/reports")
@RequiredArgsConstructor
public class SellerReportController {
    SellerReportService sellerReportService;
    SellerService sellerService;
    @GetMapping("/my")
    public ResponseEntity<SellerReport> getSellerReportHandler(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(sellerReportService.getSellerReport(sellerService.getSellerProfile(jwt)));
    }
}
