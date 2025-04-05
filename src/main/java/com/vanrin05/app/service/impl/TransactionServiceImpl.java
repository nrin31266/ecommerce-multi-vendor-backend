package com.vanrin05.app.service.impl;

import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.Transaction;
import com.vanrin05.app.repository.SellerRepository;
import com.vanrin05.app.repository.TransactionRepository;
import com.vanrin05.app.service.TransactionService;
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
    SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionsBySeller(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
