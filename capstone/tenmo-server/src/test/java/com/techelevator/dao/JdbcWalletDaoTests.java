package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.JdbcWalletDao;
import com.techelevator.tenmo.model.Wallet;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcWalletDaoTests extends BaseDaoTests{

    protected static final Wallet wallet_1 = new Wallet(1,1001, BigDecimal.valueOf(1000.00));
    protected static final Wallet wallet_2 = new Wallet(2,1002, BigDecimal.valueOf(500.00));
    protected static final Wallet wallet_3 = new Wallet(3,1003, BigDecimal.valueOf(250.00));

    private JdbcWalletDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcWalletDao(jdbcTemplate);
    }

}
