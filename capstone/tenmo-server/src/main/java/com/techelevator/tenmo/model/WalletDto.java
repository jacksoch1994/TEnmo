package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class WalletDto {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    private int userId;
    @Positive
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
