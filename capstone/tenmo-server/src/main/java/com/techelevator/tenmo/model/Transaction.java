package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    private BigDecimal amount;
    private int senderId;
    private int receiverId;
    private boolean isRequest;
    private String memo;
    private String status;
    private LocalDateTime transactionTime=LocalDateTime.now();

    /*
    #######################################   Constructors   #########################################
     */

    public Transaction(){}

    public Transaction(
            int id, BigDecimal amount, int senderId,
            int receiverId, boolean isRequest, String memo,
            String status, LocalDateTime transactionTime) {
        this.id = id;
        this.amount = amount.setScale(2,RoundingMode.HALF_UP);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.isRequest = isRequest;
        this.memo = memo;
        this.status = status;
        this.transactionTime = transactionTime;
    }

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
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
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

    /*
    ######################################  Override Methods  ########################################
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return id == transaction.id &&
                senderId==transaction.senderId &&
                receiverId == transaction.receiverId &&
                isRequest==transaction.isRequest &&
                Objects.equals(amount, transaction.amount) &&
                Objects.equals(memo, transaction.memo) &&
                Objects.equals(status, transaction.status) &&
                Objects.equals(transactionTime, transaction.transactionTime);
    }
}
