package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.UpdateSellerReportRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.SellerReportMapper;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import com.vanrin05.app.repository.SellerReportRepository;
import com.vanrin05.app.service.SellerReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SellerReportServiceImpl implements SellerReportService {

    SellerReportRepository sellerReportRepository;
    SellerReportMapper sellerReportMapper;



    @Override
    public SellerReport getSellerReport(Seller seller) {
        Optional<SellerReport> sellerReportOptional = sellerReportRepository.findBySellerId(seller.getId());

        if (sellerReportOptional.isEmpty()) {
            SellerReport newSellerReport = new SellerReport();
            newSellerReport.setSeller(seller);
            return sellerReportRepository.save(newSellerReport);
        }

        return sellerReportOptional.get();
    }

    @Override
    public SellerReport deliveredOrder(SellerOrder sellerOrder) {
        Seller seller = sellerOrder.getSeller();
        SellerReport sellerReport = getSellerReport(seller);
        sellerReport.setGrossEarnings(sellerReport.getGrossEarnings() + sellerOrder.getTotalPrice());
        sellerReport.setTotalSales(sellerReport.getTotalSales() + sellerOrder.getTotalPrice());
        sellerReport.setCompletedOrders(sellerReport.getCompletedOrders() + 1);
        sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);

        sellerReport.setNetEarnings(sellerReport.getGrossEarnings() - sellerReport.getTotalTax() - sellerReport.getTotalRefunds());
        return sellerReportRepository.save(sellerReport);
    }




}
