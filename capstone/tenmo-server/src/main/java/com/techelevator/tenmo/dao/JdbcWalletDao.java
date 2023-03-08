package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Wallet;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcWalletDao implements WalletDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcWalletDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wallet> listWallets() {
        return null;
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
