package com.vanrin05.repository;

import com.vanrin05.model.Seller;
import com.vanrin05.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
    Optional<SellerReport> findBySellerId(Long sellerId);
    List<SellerReport> findBySellerIn(List<Seller> sellers);
}
