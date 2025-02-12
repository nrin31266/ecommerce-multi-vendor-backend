package com.vanrin05.service.impl;

import com.vanrin05.model.Order;
import com.vanrin05.model.Seller;
import com.vanrin05.model.Transaction;
import com.vanrin05.repository.TransactionRepository;
import com.vanrin05.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository transactionRepository;


    @Override
    public Transaction createTransaction(Order order) {
        return null;
    }

    @Override
    public List<Transaction> getAllTransactionsBySeller(Seller seller) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }
}
