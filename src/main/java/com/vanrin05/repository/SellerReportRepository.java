package com.vanrin05.repository;

import com.vanrin05.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
    Optional<SellerReport> findBySellerId(Long sellerId);
}
