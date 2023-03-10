package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Wallet;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class WalletDao {

    private static final String API_BASE_WALLET_URL = "http://localhost:8080/wallets/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

//    public Wallet[] findAll() {
//        Wallet[] wallets = null;
//
//        try {
//            ResponseEntity<Wallet[]> response = restTemplate.exchange(API_BASE_WALLET_URL,
//                    HttpMethod.GET, makeAuthEntity(), Wallet[].class);
//            wallets = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
////                BasicLogger.log(e.getMessage());
//            System.out.println("something went wrong");
//        }
//        return wallets;
//    }

//    public Wallet[] findAll(int userId) {
//        Wallet[] wallets = null;
//
//        try {
//            ResponseEntity<Wallet[]> response = restTemplate.exchange(API_BASE_WALLET_URL+userId,
//                    HttpMethod.GET, makeAuthEntity(), Wallet[].class);
//            wallets = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
////                BasicLogger.log(e.getMessage());
//            System.out.println("something went wrong");
//        }
//        return wallets;
//    }















    /**
     * Returns an HttpEntity with the `Authorization: Bearer:` header
     */
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
