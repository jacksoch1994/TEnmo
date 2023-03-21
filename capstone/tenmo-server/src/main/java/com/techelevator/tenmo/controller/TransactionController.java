package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class TransactionController {

    /*
    ########################################   Attributes   ##########################################
     */

    private TransactionDao transDao;
    private UserDao userDao;
    private WalletDao walletDao;

    /*
   ########################################   Constructor   ##########################################
    */

    public TransactionController(TransactionDao dao, UserDao userDao, WalletDao walletDao) {
        this.transDao = dao;
        this.userDao = userDao;
        this.walletDao = walletDao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

    @GetMapping
    public List<TransactionDto> list(@RequestParam(required = false, name = "user-id") Integer userId,
                                     @RequestParam(required = false, name = "status") String status,
                                     Principal principal) {

        Integer currentUserId=userDao.findIdByUsername(principal.getName());
        boolean isAdmin = isAdmin(principal);

        //Check to see if user has permission to access other's transactions. A user can only use list Transactions if
        //they specify their own Transaction UserID unless they are an admin.
        if ((userId != null && !userId.equals(currentUserId) && !isAdmin) || (userId == null && !isAdmin)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized action: Cannot view other users' Transactions.");
        }

        List<TransactionDto> response = new ArrayList<>();
        List<Transaction> transactions;

        //Check to see if UserId and/or status is provided
        if (status != null && userId != null) {
            transactions = transDao.listTransactionsByUserIdAndStatus(userId, status);
        } else if (status != null) {
            transactions = transDao.listTransactionsByStatus(status);
        } else if (userId != null) {
            transactions = transDao.listTransactionsByUserId(userId);
        } else {
            transactions = transDao.listTransactions();
        }

        for (Transaction transaction : transactions) {
            response.add(mapTransactionToDto(transaction));
        }

        return response;
    }

    @GetMapping("/{id}")
    public TransactionDto get(@PathVariable int id, Principal principal) {

        Transaction transaction = transDao.getTransaction(id);

        //If Transaction does not exist, throw error.
        if (transaction == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Transaction");

        //Only allow the user to view the Transaction if they are the sender/receiever, or an admin
        if(!transactionBelongsToUser(transaction, principal) && !isAdmin(principal)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized action: Cannot view other users' Transactions.");
        }

        return mapTransactionToDto(transaction);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(@RequestBody @Valid CreateTransactionDto dto, Principal principal) {

        int currentUserId = userDao.findIdByUsername(principal.getName());

        //Check to see if target user exists
        if(userDao.getUserById(dto.getTargetUserId()) == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Recipient.");
        }

        //Check to see if current user is trying to pay themselves.
        if(currentUserId == dto.getTargetUserId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target user for transfer cannot match sending user.");
        }

        Transaction transaction = new Transaction(-1, dto.getAmount(), -1, -1,
                true, dto.getMemo(), "pending", LocalDateTime.now());

        if(dto.getType().equals("request")){
            //Create Update Fields on Transaction as needed for request
            transaction.setSenderId(dto.getTargetUserId());
            transaction.setReceiverId(currentUserId);

        } else if (dto.getType().equals("payment")){

            //Create Update Fields on Transaction as needed for payment
            transaction.setReceiverId(dto.getTargetUserId());
            transaction.setSenderId(currentUserId);
            transaction.setRequest(false);

            //Send to helper method to accept transaction and perform money transfer
            transaction = acceptTransaction(transaction);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must be either \"request\" or \"payment\"");
        }

        //Receive newly created Transaction from DAO and return
        Transaction newTransaction = transDao.createTransaction(transaction);
        return mapTransactionToDto(newTransaction);
    }

    //Todo: Refactor to be "Patch" -- figure out how to change this in client
    @PutMapping("/{id}")
    public TransactionDto confirmRequest(@PathVariable int id,
                                         @Valid @RequestBody TransactionStatusDto status,
                                         Principal principal) {

        Transaction transaction = transDao.getTransaction(id);

        //Check to see if Transaction exists
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Transaction");
        }

        //Check to see if user is the sender for this Transaction.
        if (transaction.getSenderId() != userDao.findIdByUsername(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized. Cannot confirm another user's incoming request.");
        }

        //Check to see if Transaction is in valid status
        if (!transaction.getStatus().equals("pending")) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Cannot update a transaction that " +
                    "is not in the \"pending\" status.");
        }

        //Check to see if incoming DTO has a valid value in the status field
        if (!status.getStatus().equals("accepted") && !status.getStatus().equals("rejected")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for \"status\". " +
                    "Status must be either \"accepted\" or \"rejected\".");
        }

        //Update Transaction Time to be the current date.
        transaction.setTransactionTime(LocalDateTime.now());

        if (status.getStatus().equals("accepted")) {
            transaction = acceptTransaction(transaction);

        } else if (status.getStatus().equals("rejected")) {
            transaction.setStatus("rejected");
        }

        //Return Updated Transaction
        transDao.updateTransaction(id, transaction);
        return mapTransactionToDto(transDao.getTransaction(id));
    }

    /*
    ########################################  Helper Methods  ##########################################
     */

    private boolean canMakeWalletTransfer(int senderWalletId, int receiverWalletId, BigDecimal amount) {
        Wallet sender = walletDao.getWallet(senderWalletId);
        Wallet receiver = walletDao.getWallet(receiverWalletId);

        //If either wallet does not exists, throw an error
        if (sender == null) {
            //Todo: Change description to match error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't process request.");
        } else if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown wallet for Receiving Wallet.");
        }

        //Make sure sender has money in their account to make transfer
        return sender.getBalance().compareTo(amount) >= 0;
    }

    private TransactionDto mapTransactionToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();

        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setMemo(transaction.getMemo());
        dto.setReceiverId(transaction.getReceiverId());
        dto.setSenderId(transaction.getSenderId());
        dto.setRequest(transaction.isRequest());
        dto.setStatus(transaction.getStatus());
        dto.setTransactionTime(transaction.getTransactionTime());

        return dto;
    }

    private boolean isAdmin(Principal principal) {
        int currentUserId=userDao.findIdByUsername(principal.getName());
        return userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"));
    }

    private boolean transactionBelongsToUser(Transaction transaction, Principal principal) {
        int currentUserId=userDao.findIdByUsername(principal.getName());
        return transaction.getReceiverId() == currentUserId || transaction.getSenderId() == currentUserId;
    }

    private Transaction acceptTransaction(Transaction transaction) {

        //Get user Ids
        int senderId = transaction.getSenderId();
        int receiverId = transaction.getReceiverId();

        //Todo: Send in wallets instead of ID
        //Get wallet Ids for each User
        int senderWalletId = walletDao.getWalletByUser(senderId).getId();
        int receiverWalletId = walletDao.getWalletByUser(receiverId).getId();

        //Check to see if sending wallet has sufficient funds
        //Todo: Send in wallets instead of ID
        if (!canMakeWalletTransfer(senderWalletId, receiverWalletId, transaction.getAmount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The wallet to transfer funds from has an " +
                    "insufficient balance to make payment.");
        }

        //Call DAO transfer balance method
        //Todo: Catch potential exceptions and return appropriate HTTP status
        walletDao.transferBalance(senderWalletId, receiverWalletId, transaction.getAmount());

        //Update original transaction
        transaction.setStatus("accepted");

        return transaction;
    }

}
