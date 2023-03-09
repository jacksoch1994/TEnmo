package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    @Positive
    private BigDecimal amount;
    private int senderId;
    private int receiverId;
    private boolean isRequest;
    private String memo;
    @NotBlank
    private String status;
    @NotNull
    private LocalDateTime transactionTime=LocalDateTime.now();

}
