package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class TransactionStatusDto {

    /*
    ########################################   Attributes   ##########################################
     */

    @NotBlank
    private String status;

    /*
    ######################################## Getter Methods ##########################################
     */

    public String getStatus() {
        return status;
    }

    /*
    ######################################## Setter Methods ##########################################
     */

    public void setStatus(String status) {
        this.status = status;
    }
}
