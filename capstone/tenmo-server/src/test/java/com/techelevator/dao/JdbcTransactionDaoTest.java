package com.techelevator.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.dao.JdbcTransactionDao;
import com.techelevator.tenmo.dao.JdbcWalletDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Wallet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JdbcTransactionDaoTest extends BaseDaoTests{

    private TransactionDao sut;

    private final Transaction TRANS_1 = new Transaction(1, new BigDecimal("100.22"), 1001, 1003,
            true, "", "pending",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_2= new Transaction(2, new BigDecimal("100.22"), 1002, 1003,
            false, "this is a memo", "pending",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_3 = new Transaction(3, new BigDecimal("100.22"), 1002, 1003,
            true, "this is also a memo", "accepted",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_4 = new Transaction(4, new BigDecimal("100.22"), 1001, 1002,
            false, "this is also a memo", "rejected",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_5 = new Transaction(5, new BigDecimal("100.22"), 1002, 1003,
            false, "this is also a memo", "pending",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_6 = new Transaction(6, new BigDecimal("112.03"), 1002, 1003,
            false, "this is a memo", "rejected",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_7 = new Transaction(7, new BigDecimal("110.22"), 1001, 1003,
            true, "this is a memo", "pending",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));
    private final Transaction TRANS_8 = new Transaction(8, new BigDecimal("100.22"), 1003, 1001,
            false, "this is also a memo", "rejected",
            LocalDateTime.of(1111, 11, 11, 11, 11, 11));

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransactionDao(jdbcTemplate);
    }

    @Test
    public void listTransactions_returns_all_transactions() {
        List<Transaction> transactions = sut.listTransactions();

        //Test for non-null return value
        Assert.assertNotNull(transactions);
        //Test for correct List size
        Assert.assertEquals(8, transactions.size());
        Assert.assertEquals(TRANS_1, transactions.get(0));
        Assert.assertEquals(TRANS_2, transactions.get(1));
        Assert.assertEquals(TRANS_3, transactions.get(2));
        Assert.assertEquals(TRANS_4, transactions.get(3));
        Assert.assertEquals(TRANS_5, transactions.get(4));
        Assert.assertEquals(TRANS_6, transactions.get(5));
        Assert.assertEquals(TRANS_7, transactions.get(6));
        Assert.assertEquals(TRANS_8, transactions.get(7));
    }

    @Test
    public void getTransaction_returns_correct_transaction_given_valid_id() {
        Assert.assertEquals(TRANS_2, sut.getTransaction(TRANS_2.getId()));
    }

    @Test
    public void getTransaction_returns_null_given_invalid_id() {
        Assert.assertNull(sut.getTransaction(-1));
    }

    @Test
    public void getTransactionByUser_returns_list_with_correct_transactions_given_valid_user_id() {
        List<Transaction> transactions = sut.listTransactionsByUserId(1001);

        //Test for non-null return value
        Assert.assertNotNull(transactions);
        //Test for correct List size
        Assert.assertEquals(4, transactions.size());
        Assert.assertEquals(TRANS_1, transactions.get(0));
        Assert.assertEquals(TRANS_4, transactions.get(1));
        Assert.assertEquals(TRANS_7, transactions.get(2));
        Assert.assertEquals(TRANS_8, transactions.get(3));
    }

    @Test
    public void getTransactionByUser_returns_empty_list_given_invalid_user_id() {
        Assert.assertEquals(sut.listTransactionsByUserId(-1).size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTransaction_given_null_throws_exception() {
        sut.updateTransaction(1, null);
    }

    @Test
    public void updated_transaction_had_expected_values_when_retrieved() {
        Transaction transaction = new Transaction(-1, new BigDecimal("100.00"), 1003, 1002,
                true, "gimme money", "accepted",
                LocalDateTime.of(2011, 11, 11, 11, 11, 11));
        Transaction actual = sut.updateTransaction(TRANS_1.getId(), transaction);

        transaction.setId(actual.getId());
        Assert.assertEquals(transaction, sut.getTransaction(actual.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTransaction_given_null_throws_transaction() {
        sut.createTransaction(null);
    }

    @Test
    public void createdTransaction_has_id_and_expected_values() {
        Transaction transaction = new Transaction(-1, new BigDecimal("512.00"), 1001, 1002,
                true, "no", "pending", LocalDateTime.now());
        Transaction createdTransaction = sut.createTransaction(transaction);
        int createdId = createdTransaction.getId();
        Assert.assertTrue(createdId>0);

        transaction.setId(createdId);
        Assert.assertEquals(transaction, createdTransaction);
    }






}
