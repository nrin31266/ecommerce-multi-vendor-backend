package com.vanrin05.service;

import com.vanrin05.model.Order;
import com.vanrin05.model.Seller;
import com.vanrin05.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getAllTransactionsBySeller(Seller seller);

    List<Transaction> getAllTransactions();

}
