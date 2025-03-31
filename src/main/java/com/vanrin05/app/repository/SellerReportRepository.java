package com.vanrin05.app.repository;

import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
    Optional<SellerReport> findBySellerId(Long sellerId);
    List<SellerReport> findBySellerIn(List<Seller> sellers);
}
