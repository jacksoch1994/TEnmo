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
    @Positive(message="Invalid User ID")
    private int targetUserId;
    @Positive(message="Transaction amount must be positive.")
    private BigDecimal amount;
    private String memo;
    @JsonProperty("transaction-type")
    @NotBlank(message="The type of transaction must be specified.")
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
