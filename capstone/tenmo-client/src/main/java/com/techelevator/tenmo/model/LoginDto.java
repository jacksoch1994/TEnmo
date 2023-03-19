package com.techelevator.tenmo.model;

public class LoginDto {

   /*
    ########################################   Attributes   ##########################################
    */

    private String username;
    private String password;

    /*
    ########################################   Constructor   ##########################################
    */

    public LoginDto() {};

   /*
    ######################################## Getter Methods ##########################################
    */

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

   /*
    ######################################## Setter Methods ##########################################
    */

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   /*
    ######################################  Override Methods  ########################################
    */

    @Override
    public String toString() {
        return "LoginDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
