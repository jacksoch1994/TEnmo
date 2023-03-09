package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class WalletDto {

    /*
    ########################################   Attributes   ##########################################
     */

    public int id;
    public int userId;
    @Positive
    public BigDecimal balance;

}
