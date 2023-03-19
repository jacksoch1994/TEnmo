package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.LoginDto;
import com.techelevator.tenmo.model.LoginResponseDto;
import com.techelevator.tenmo.model.RegisterUser;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class LoginDao {

    /*
    ####################################### Constant Values ##########################################
     */

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate TEMPLATE = new RestTemplate();

    /*
    ###########################################  Methods  ############################################
     */

    public String login(String username, String password) {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDto> entity = new HttpEntity<>(loginDto, headers);

        String token = null;
        try {
            ResponseEntity<LoginResponseDto> response = TEMPLATE.exchange(
                    API_BASE_URL + "login",
                    HttpMethod.POST,
                    entity,
                    LoginResponseDto.class);
            LoginResponseDto body = response.getBody();
            if (body != null) {
                token = body.getToken();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("something went wrong");
//            BasicLogger.log(e.getMessage());
        }
        return token;
    }

    public boolean register(String username, String password) {
        RegisterUser newUser = new RegisterUser();
        newUser.setUsername(username);
        newUser.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterUser> entity = new HttpEntity<>(newUser, headers);

        boolean successful = false;

        try {
            TEMPLATE.exchange(
                    API_BASE_URL + "register",
                    HttpMethod.POST,
                    entity,
                    Void.class);

            successful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("something went wrong");
//            BasicLogger.log(e.getMessage());
        }

        return successful;
    }

}
