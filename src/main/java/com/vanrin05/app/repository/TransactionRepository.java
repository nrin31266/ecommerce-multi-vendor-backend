package com.vanrin05.app.repository;

import com.vanrin05.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySellerId(Long sellerId);
}
