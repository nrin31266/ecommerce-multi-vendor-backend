package com.vanrin05.service.impl;

import com.vanrin05.dto.request.UpdateSellerReportRequest;
import com.vanrin05.exception.AppException;
import com.vanrin05.mapper.SellerReportMapper;
import com.vanrin05.model.Seller;
import com.vanrin05.model.SellerReport;
import com.vanrin05.repository.SellerReportRepository;
import com.vanrin05.service.SellerReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SellerReportServiceImpl implements SellerReportService {

    SellerReportRepository sellerReportRepository;
    SellerReportMapper sellerReportMapper;
    @Override
    public List<SellerReport> findSellerReportBySellers(List<Seller> sellers) {
        return sellerReportRepository.findBySellerIn(sellers);
    }

    @Override
    public List<SellerReport> updateSellerReports(List<SellerReport> sellerReports) {
        return sellerReportRepository.saveAll(sellerReports);
    }


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
    public SellerReport updateSellerReport(UpdateSellerReportRequest updateSellerReportRequest, Long sellerId) {
        SellerReport sellerReport = sellerReportRepository.findBySellerId(sellerId).orElseThrow(()->new AppException("Seller report not found"));
        sellerReportMapper.toUpdateSellerReport(sellerReport, updateSellerReportRequest);
        return sellerReportRepository.save(sellerReport);
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }


}
