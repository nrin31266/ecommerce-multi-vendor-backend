package com.vanrin05.app.service;

import com.vanrin05.app.model.Order;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getAllTransactionsBySeller(Seller seller);

    List<Transaction> getAllTransactions();

}
