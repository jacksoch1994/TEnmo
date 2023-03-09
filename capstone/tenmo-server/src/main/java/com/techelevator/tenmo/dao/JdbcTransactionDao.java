package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDao implements TransactionDao{

    /*
    ########################################   Attributes   ##########################################
     */

    private final JdbcTemplate jdbcTemplate;

    /*
    ########################################   Constructor   ##########################################
     */

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    #########################################  DAO Methods  ###########################################
     */

    @Override
    public List<Transaction> listTransactionsByUserId(int userId) {

        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM money_transaction WHERE sender_id=? OR receiver_id=?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while(rs.next()){
            Transaction transaction = mapRowToTransaction(rs);
            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public List<Transaction> listTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM money_transaction;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

        while(rs.next()){
            Transaction transaction = mapRowToTransaction(rs);
            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        String sql = "SELECT * FROM money_transaction WHERE transaction_id=?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transactionId);
        if(result.next()){
            return mapRowToTransaction(result);
        } else {
            return null;
        }
    }

    @Override
    public Transaction updateTransaction(int transactionId, Transaction transactionToUpdate) {
        if (transactionToUpdate==null){
            throw new IllegalArgumentException();
        }

        String sql = "UPDATE money_transaction SET " +
                "sender_id=?, " +
                "receiver_id=?, " +
                "status=?, " +
                "is_request=?, " +
                "amount=?, " +
                "memo=?, " +
                "transaction_time=? " +
                "WHERE transaction_id=? RETURNING transaction_id;";
        Integer id = jdbcTemplate.queryForObject(sql,Integer.class,transactionToUpdate.getSenderId(),
                transactionToUpdate.getReceiverId(), transactionToUpdate.getStatus(),
                transactionToUpdate.isRequest(), transactionToUpdate.getAmount(), transactionToUpdate.getMemo(),
                transactionToUpdate.getTransactionTime(), transactionId);

        if(id!=null){
            return getTransaction(id);
        } else {
            return null;
        }
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        if (transaction==null){
            throw new IllegalArgumentException();
        }

        String sql = "INSERT INTO money_transaction(\n" +
                "\t sender_id, receiver_id, status, is_request, amount, memo, transaction_time)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?) RETURNING transaction_id;";
        Integer newTransactionId = jdbcTemplate.queryForObject(sql, Integer.class,
                transaction.getSenderId(), transaction.getReceiverId(),
                transaction.getStatus(), transaction.isRequest(),
                transaction.getAmount(), transaction.getMemo(), transaction.getTransactionTime());
        if(newTransactionId!=null){
            return getTransaction(newTransactionId);
        } else {
            return null;
        }
    }

    /*
    ########################################  Helper Methods  ##########################################
     */

    private Transaction mapRowToTransaction(SqlRowSet rs){
        Transaction transaction = new Transaction();
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setMemo(rs.getString("memo"));
        transaction.setId(rs.getInt("transaction_id"));
        transaction.setReceiverId(rs.getInt("receiver_id"));
        transaction.setSenderId(rs.getInt("sender_id"));
        transaction.setStatus(rs.getString("status"));
        transaction.setRequest(rs.getBoolean("is_request"));
        if(rs.getTimestamp("transaction_time")!=null) {
            transaction.setTransactionTime(rs.getTimestamp("transaction_time").toLocalDateTime());
        }
        return transaction;
    }
}
