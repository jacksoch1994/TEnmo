package com.techelevator.tenmo.model;

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

    /*
    ######################################## Getter Methods ##########################################
     */

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public String getMemo() {
        return memo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    /*
    ######################################## Setter Methods ##########################################
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }



}
