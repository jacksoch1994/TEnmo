package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private int id;
    private BigDecimal amount;
    private int senderId;
    private int receiverId;
    private boolean isRequest;
    private String memo;
    private String status;
    private LocalDateTime transactionTime;

    public Transaction(
            int id, BigDecimal amount, int senderId,
            int receiverId, boolean isRequest, String memo,
            String status, LocalDateTime transactionTime) {
        this.id = id;
        this.amount = amount;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.isRequest = isRequest;
        this.memo = memo;
        this.status = status;
        this.transactionTime = transactionTime;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
}
