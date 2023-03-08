package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcWalletDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Wallet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcWalletDaoTests extends BaseDaoTests{

    protected static final Wallet WALLET_1 = new Wallet(1,1001, BigDecimal.valueOf(1000.00));
    protected static final Wallet WALLET_2 = new Wallet(2,1002, BigDecimal.valueOf(500.00));
    protected static final Wallet WALLET_3 = new Wallet(3,1003, BigDecimal.valueOf(250.00));

    private JdbcWalletDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcWalletDao(jdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWallet_given_null_throws_exception() {
        sut.updateWallet(null, 1);
    }

    @Test
    public void updated_wallet_has_expected_values_when_retrieved(){
        Wallet wallet = sut.getWallet(1);
        wallet.setBalance(BigDecimal.valueOf(1001.00));
        wallet.setUserId(1002);

        sut.updateWallet(wallet,1);

        Assert.assertEquals(wallet, sut.getWallet(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWallet_given_null_throws_exception() {
        sut.createWallet(null, 1001);
    }

    @Test
    public void create_wallet_returns_wallet_with_id_and_expected_values(){
        Wallet wallet = new Wallet(4,1002,BigDecimal.ONE);
        Wallet createdWallet = sut.createWallet(wallet, 1002);
        int idOfCreatedWallet = createdWallet.getId();
        Assert.assertTrue(idOfCreatedWallet>0);

        wallet.setId(idOfCreatedWallet);
        Assert.assertEquals(wallet, createdWallet);
    }

    @Test()
    public void getWallet_returns_correct_wallet_when_given_valid_id(){
        Assert.assertEquals(sut.getWallet(1), WALLET_1);
    }

    @Test
    public void getWallet_returns_null_when_given_bad_id(){
        Assert.assertNull(sut.getWallet(-5));
    }

    @Test()
    public void getWalletByUser_returns_correct_wallet_when_given_valid_id(){
        Assert.assertEquals(sut.getWalletByUser(1001), WALLET_1);
    }

    @Test
    public void getWalletByUser_returns_null_when_given_bad_id(){
        Assert.assertNull(sut.getWalletByUser(-5000));
    }

    @Test
    public void listWallets_returns_all_wallets() {
        List<Wallet> wallets = sut.listWallets();

        Assert.assertNotNull(wallets);
        Assert.assertEquals(3, wallets.size());
        Assert.assertEquals(WALLET_1, wallets.get(0));
        Assert.assertEquals(WALLET_2, wallets.get(1));
        Assert.assertEquals(WALLET_3, wallets.get(2));
    }


}
