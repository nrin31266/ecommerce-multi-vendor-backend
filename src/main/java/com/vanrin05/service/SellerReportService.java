package com.vanrin05.service;


import com.vanrin05.dto.request.UpdateSellerReportRequest;
import com.vanrin05.model.Seller;
import com.vanrin05.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(UpdateSellerReportRequest updateSellerReportRequest, Long sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);

}
