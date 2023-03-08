package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTransactionDao implements TransactionDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
