package com.vanrin05.app.service;

import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.orderpayment.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getAllTransactionsBySeller(Seller seller);

    List<Transaction> getAllTransactions();

}
