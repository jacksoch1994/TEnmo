package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.CreateTransactionDto;
import com.techelevator.tenmo.model.RegisterUser;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private static final String API_BASE_TRANSACTION_URL = "http://localhost:8080/transactions/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String token) {
        authToken = token;
    }

    public Transaction[] getOwnTransactions(int ownId){
        ResponseEntity<Transaction[]> transactions=null;
        try {
            transactions = restTemplate.exchange(
                    API_BASE_TRANSACTION_URL + "?status=pending&user-id=" + ownId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transaction[].class);
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transactions.getBody();
    }

    public boolean makePayment(int targetUserId, BigDecimal amount, String memo){
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(targetUserId, amount, memo, "payment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<CreateTransactionDto> entity = new HttpEntity<>(createTransactionDto, headers);

        boolean successful = false;

        try {
            restTemplate.exchange(
                    API_BASE_TRANSACTION_URL,
                    HttpMethod.POST,
                    entity,
                    Void.class);
            successful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
//            BasicLogger.log(e.getMessage());
        }
        return successful;
    }

    public boolean requestPayment(int targetUserId, BigDecimal amount, String memo){
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(targetUserId, amount, memo, "request");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<CreateTransactionDto> entity = new HttpEntity<>(createTransactionDto, headers);

        boolean successful = false;

        try {
            restTemplate.exchange(
                    API_BASE_TRANSACTION_URL,
                    HttpMethod.POST,
                    entity,
                    Void.class);
            successful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
//            BasicLogger.log(e.getMessage());
        }
        return successful;
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
