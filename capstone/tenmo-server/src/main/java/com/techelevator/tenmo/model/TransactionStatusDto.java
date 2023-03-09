package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class TransactionStatusDto {

    @NotBlank
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
