package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserDao {

    /*
    ####################################### Constant Values ##########################################
     */

    private static final String API_BASE_USER_URL = "http://localhost:8080/users/";
    private final RestTemplate TEMPLATE = new RestTemplate();

    /*
    ########################################   Attributes   ##########################################
     */

    private String authToken = null;

    /*
    ###########################################  Methods  ############################################
     */

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User[] findAll() {
        User[] users = null;

        try {
            ResponseEntity<User[]> response = TEMPLATE.exchange(API_BASE_USER_URL,
                    HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
//                BasicLogger.log(e.getMessage());
            System.out.println("something went wrong");
        }
        return users;
    }

    public User findUserById(int id){
        User user = null;
        try {
            ResponseEntity<User> response = TEMPLATE.exchange(API_BASE_USER_URL + id,
                    HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
//                BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return user;
    }

    public User findOwnUser(){
        User user = null;
        try{
            ResponseEntity<User> response = TEMPLATE.exchange(API_BASE_USER_URL + "me",
                    HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
//                BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return user;

    }

    /**
     * Returns an HttpEntity with the `Authorization: Bearer:` header
     */
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
