package com.vanrin05.service;


import com.vanrin05.dto.request.UpdateSellerReportRequest;
import com.vanrin05.model.Seller;
import com.vanrin05.model.SellerReport;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(UpdateSellerReportRequest updateSellerReportRequest, Long sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);
    List<SellerReport> findSellerReportBySellers(List<Seller> sellers);
    List<SellerReport> updateSellerReports(List<SellerReport> sellerReports);
}
