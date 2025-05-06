package com.vanrin05.app.service;


import com.vanrin05.app.dto.request.UpdateSellerReportRequest;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;
import com.vanrin05.app.model.orderpayment.SellerOrder;

import java.util.List;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport deliveredOrder(SellerOrder sellerOrder);
}
