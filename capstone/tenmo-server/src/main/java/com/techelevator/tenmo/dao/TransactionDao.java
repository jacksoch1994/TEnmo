package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.util.List;

public interface TransactionDao {

    List<Transaction> listTransactionsByUserId(int userId);  //list of transactions for one user

    List<Transaction> listTransactions();  //list of all transactions for use by admin only

    Transaction getTransaction(int transactionId); //get individual transaction

    Transaction updateTransaction(int transactionId, Transaction transactionToUpdate); //change transaction. not sure if we need transaction ID

    Transaction createTransaction(Transaction transaction);
}
