package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Wallet {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    private int userId;
    private BigDecimal balance;

    /*
    #######################################   Constructors   #########################################
     */

    public Wallet(int id, int userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public Wallet() {}

    /*
    ######################################## Getter Methods ##########################################
     */

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /*
    ######################################## Setter Methods ##########################################
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    /*
    ######################################  Override Methods  ########################################
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id &&
                userId==wallet.userId &&
                Objects.equals(balance, wallet.balance);
    }
}
