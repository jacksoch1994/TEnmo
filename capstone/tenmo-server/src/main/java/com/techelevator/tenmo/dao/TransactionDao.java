package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.util.List;

public interface TransactionDao {

    /**
     * Obtains a List of all Transactions.
     *
     * @return a List containing all Transactions.
     */
    List<Transaction> listTransactions();  //list of all transactions for use by admin only

    /**
     * Obtains a List of all Transactions where the specified User Id is either the sender or receiver.
     *
     * @param userId the id of the User.
     * @return a List of all Transactions involving the specified User.
     */
    List<Transaction> listTransactionsByUserId(int userId);  //list of transactions for one user

    List<Transaction> listTransactionsByStatus(String status);

    List<Transaction> listTransactionsByUserIdAndStatus(int userId, String status);

    /**
     * Obtains a single Transaction based on the provided id. Returns null if no Transaction with the provided
     * transactionId exists.
     *
     * @param transactionId the id of the Transaction to get.
     * @return a Transaction object representing the Transaction with this provided id.
     */
    Transaction getTransaction(int transactionId); //get individual transaction

    /**
     * Updates the values of a Transaction.
     *
     * @param transactionId the id of the Transaction to update.
     * @param transactionToUpdate a Transaction object containing updated values.
     * @returna Transaction object representing updated Transaction with this provided id.
     */
    Transaction updateTransaction(int transactionId, Transaction transactionToUpdate); //change transaction. not sure if we need transaction ID

    /**
     * Creates a new Transaction.
     *
     * @param transaction a Transaction object representing the Transaction to add.
     * @return the newly created Transaction.
     */
    Transaction createTransaction(Transaction transaction);
}
