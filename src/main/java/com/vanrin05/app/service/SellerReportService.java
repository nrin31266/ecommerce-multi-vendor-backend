package com.vanrin05.app.service;


import com.vanrin05.app.dto.request.UpdateSellerReportRequest;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;

import java.util.List;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(UpdateSellerReportRequest updateSellerReportRequest, Long sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);
    List<SellerReport> findSellerReportBySellers(List<Seller> sellers);
    List<SellerReport> updateSellerReports(List<SellerReport> sellerReports);
}
