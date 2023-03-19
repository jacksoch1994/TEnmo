package com.techelevator.tenmo.model;

public class User {

    /*
    ########################################   Attributes   ##########################################
     */

    private int id;
    private String username;

    /*
    ######################################## Setter Methods ##########################################
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /*
    ######################################## Getter Methods ##########################################
     */

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

}
