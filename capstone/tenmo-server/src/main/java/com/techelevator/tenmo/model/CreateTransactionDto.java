package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateTransactionDto {

    /*
    ########################################   Attributes   ##########################################
     */

    @JsonProperty("target-user")
    @Positive
    private int targetUserId;
    @Positive
    private BigDecimal amount;
    private String memo;
    @JsonProperty("transaction-type")
    @NotBlank
    private String type;

    /*
    ########################################   Constructor   ##########################################
     */

    public CreateTransactionDto(){}

    /*
    ######################################## Getter Methods ##########################################
     */
    public int getTargetUserId() {
        return targetUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMemo() {
        return memo;
    }

    public String getType() {
        return type;
    }

    /*
    ######################################## Setter Methods ##########################################
     */

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setType(String type) {
        this.type = type;
    }




}
