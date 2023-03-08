package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Wallet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcWalletDao implements WalletDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcWalletDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wallet> listWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM user_wallet;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

        while(rs.next()){
            Transaction transaction = mapRowToTransaction(rs);
            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public Wallet getWallet(int walletId) {
        return null;
    }

    @Override
    public Wallet getWalletByUser(int userId) {
        return null;
    }

    @Override
    public Wallet updateWallet(Wallet updatedWallet, int walletId) {
        return null;
    }

    @Override
    public Wallet createWallet(Wallet newWallet, int userId) {
        return null;
    }
}
