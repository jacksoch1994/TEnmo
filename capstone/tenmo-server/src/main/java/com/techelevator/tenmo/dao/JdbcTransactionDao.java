package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.util.List;

public class JdbcTransactionDao implements TransactionDao{
    @Override
    public List<Transaction> listTransactionsByUserId(int userId) {
        return null;
    }

    @Override
    public List<Transaction> listTransactions() {
        return null;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        return null;
    }

    @Override
    public Transaction updateTransaction(int transactionId, Transaction transactionToUpdate) {
        return null;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return null;
    }
}
