package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Wallet {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    private int userId;
    private BigDecimal balance;

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
        this.balance = balance;
    }

}
