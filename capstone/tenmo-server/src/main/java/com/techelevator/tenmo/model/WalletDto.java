package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class WalletDto {

    private int id;
    private int userId;
    @Positive
    private BigDecimal balance;

}
