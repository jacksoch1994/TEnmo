package com.techelevator.tenmo.model;
/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, CredentialsDto represents the data a client must
    pass to the server for a login endpoint, and TokenDto represents the object that's returned from the server
    to the client from a login endpoint.
 */
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
