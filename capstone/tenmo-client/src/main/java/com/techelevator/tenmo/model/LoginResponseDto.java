package com.techelevator.tenmo.model;

public class LoginResponseDto {

    /*
    ########################################   Attributes   ##########################################
    */

    private String token;
    private User user;

    /*
    ########################################   Constructor   ##########################################
    */

    public LoginResponseDto(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public LoginResponseDto() {

    }

    /*
    ######################################## Getter Methods ##########################################
    */

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    /*
    ######################################## Setter Methods ##########################################
    */

    void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
