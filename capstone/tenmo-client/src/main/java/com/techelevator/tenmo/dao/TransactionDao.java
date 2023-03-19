package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.CreateTransactionDto;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.UpdateTransactionDto;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransactionDao {

    /*
    ####################################### Constant Values ##########################################
     */

    private static final String API_BASE_TRANSACTION_URL = "http://localhost:8080/transactions/";
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

    public Transaction[] getTransactionHistory(int ownId){
        ResponseEntity<Transaction[]> transactions=null;
        try {
            transactions = TEMPLATE.exchange(
                    API_BASE_TRANSACTION_URL + "?user-id=" + ownId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transaction[].class);
        } catch (RestClientResponseException e) {
            //return empty array if exception is received
            return new Transaction[0];
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing Transaction resource.");
        }
        //Return empty array if response is null
        return (transactions == null) ? new Transaction[0] : transactions.getBody();
    }


    //only pending transactions
    public Transaction[] getOwnTransactions(int ownId){
        ResponseEntity<Transaction[]> transactions=null;
        try {
            transactions = TEMPLATE.exchange(
                    API_BASE_TRANSACTION_URL + "?status=pending&user-id=" + ownId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transaction[].class);
        } catch (RestClientResponseException e) {
            //return empty array if exception is received
            return new Transaction[0];
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing Transaction resource.");
        }
        //Return empty array if response is null
        return (transactions == null) ? new Transaction[0] : transactions.getBody();
    }

    public boolean makePayment(int targetUserId, BigDecimal amount, String memo){
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(targetUserId, amount, memo, "payment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<CreateTransactionDto> entity = new HttpEntity<>(createTransactionDto, headers);

        boolean successful = false;

        try {
            TEMPLATE.exchange(
                    API_BASE_TRANSACTION_URL,
                    HttpMethod.POST,
                    entity,
                    Void.class);
            successful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
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
            TEMPLATE.exchange(
                    API_BASE_TRANSACTION_URL,
                    HttpMethod.POST,
                    entity,
                    Void.class);
            successful = true;

        } catch (RestClientResponseException e) {
            //return empty array if exception is received
            System.out.println(e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing Transaction resource.");
        }
        return successful;
    }

    public boolean acceptOrRejectPendingTransaction(int transactionToUpdate, String newStatus){
        UpdateTransactionDto updateTransactionDto = new UpdateTransactionDto();
        updateTransactionDto.setStatus(newStatus);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<UpdateTransactionDto> entity = new HttpEntity<>(updateTransactionDto, headers);

        boolean successful = false;

        try {
            TEMPLATE.exchange(
                    API_BASE_TRANSACTION_URL + transactionToUpdate,
                    HttpMethod.PUT,
                    entity,
                    Void.class);
            successful = true;
        } catch (RestClientResponseException e) {
            //return empty array if exception is received
            System.out.println(e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing Transaction resource.");
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
