package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Wallet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcWalletDao implements WalletDao{

    /*
    ########################################   Attributes   ##########################################
     */

    private final JdbcTemplate jdbcTemplate;

    /*
    ########################################   Constructor   ##########################################
     */

    public JdbcWalletDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    #########################################  DAO Methods  ###########################################
     */

    @Override
    public List<Wallet> listWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT wallet_id, user_id, balance FROM user_wallet;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

        while(rs.next()){
            wallets.add(mapRowToWallet(rs));
        }

        return wallets;
    }

    @Override
    public Wallet getWallet(int walletId) {
        Wallet wallet = null;

        String sql = "SELECT wallet_id, user_id, balance FROM user_wallet WHERE wallet_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, walletId);

        if (rows.next()) {
            wallet = mapRowToWallet(rows);
        }

        return wallet;
    }

    @Override
    public Wallet getWalletByUser(int userId) {
        Wallet wallet = null;

        String sql = "SELECT wallet_id, user_id, balance FROM user_wallet WHERE user_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId);

        if (rows.next()) {
            wallet = mapRowToWallet(rows);
        }

        return wallet;
    }

    //Todo: remove dao method since not being used and for security reasons
    @Override
    public Wallet updateWallet(Wallet updatedWallet, int walletId) {

        if (updatedWallet == null) throw new IllegalArgumentException("Updated Wallet Cannot Be Null.");
        if (getWallet(walletId) == null) return null;

        String sql = "UPDATE user_wallet " +
                "SET user_id=?, balance=? " +
                "WHERE wallet_id = ?;";

        jdbcTemplate.update(sql, updatedWallet.getUserId(), updatedWallet.getBalance(), walletId);

        return getWallet(walletId);
    }

    @Override
    //Todo: Remove true / false.
    public boolean transferBalance(int sendingWalletId, int receivingWalletId, BigDecimal transferAmount) {
        String sql = "UPDATE user_wallet \n" +
                "SET balance = balance - ?\n" +
                "WHERE wallet_id = ?;\n" +
                "UPDATE user_wallet \n" +
                "SET balance = balance + ?\n" +
                "WHERE wallet_id = ?;\n";

        //Todo: Raise server errors -- false does not give enough information to determine what is going on ex.) WalletNotFoundException ex.) NotEnoughFundsException
        //If not enough money in sending wallet or wither wallet is null, return false
        if (getWallet(sendingWalletId) == null || getWallet(receivingWalletId) == null
                || getWallet(sendingWalletId).getBalance().compareTo(transferAmount) < 0) {
            return false;
        }

        try {
            //Todo: Make sure this properly affects the proper number of rows (i.e. 2 rows) Should also throw exception and rollback.
            return jdbcTemplate.update(sql, transferAmount, sendingWalletId, transferAmount, receivingWalletId) > 0;
        } catch (DataAccessException e) {
            //ToDo: Handle this error properly without false.
            return false;
        }

    }

    @Override
    public Wallet createWallet(Wallet newWallet, int userId) {
        if (newWallet == null) throw new IllegalArgumentException("New Wallet cannot be Null.");

        String sql = "INSERT INTO user_wallet(balance, user_id) VALUES (?, ?) RETURNING wallet_id;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, newWallet.getBalance(), userId);

        if(id!=null){
            return getWallet(id);
        } else {
            return null;
        }
    }

    /*
    ########################################  Helper Methods  ##########################################
     */

    private Wallet mapRowToWallet(SqlRowSet row) {
        Wallet wallet = new Wallet();
        wallet.setUserId(row.getInt("user_id"));
        wallet.setId(row.getInt("wallet_id"));
        wallet.setBalance(row.getBigDecimal("balance"));

        return wallet;
    }
}
