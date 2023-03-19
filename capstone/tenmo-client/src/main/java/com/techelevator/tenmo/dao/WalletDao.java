package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Wallet;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class WalletDao {

    /*
    ####################################### Constant Values ##########################################
     */

    private static final String API_BASE_WALLET_URL = "http://localhost:8080/wallets/";
    private final RestTemplate TEMPLATE = new RestTemplate();

    /*
    ########################################   Attributes   ##########################################
     */

    private String authToken = null;

    /*
    ###########################################  Methods  ############################################
     */

    public void setAuthToken(String token) {
        authToken = token;
    }

    public Wallet getUserWallet() {
        Wallet wallet = null;

        try {
            ResponseEntity<Wallet> response = TEMPLATE.exchange(API_BASE_WALLET_URL + "me",
                    HttpMethod.GET, makeAuthEntity(), Wallet.class);
            wallet = response.getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing Wallet Resource.");
        }
        return wallet;
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
