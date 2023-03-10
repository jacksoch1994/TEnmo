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
    public List<TransactionDto> list(@RequestParam(required = false, name = "user-id") Integer userId , Principal principal) {

        Integer currentUserId=userDao.findIdByUsername(principal.getName());

        List<TransactionDto> response = new ArrayList<>();
        List<Transaction> transactions;

        //Check to see if UserId is provided
        if (userId != null) {
            if(!userId.equals(currentUserId) && !userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"))){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your transactions.");
            }
            transactions = transDao.listTransactionsByUserId(userId);
        } else {
            if(!userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"))){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your transactions.");
            }
            transactions = transDao.listTransactions();
        }

        for (Transaction transaction : transactions) {
            response.add(mapTransactionToDto(transaction));
        }

        return response;
    }

    @GetMapping("/{id}")
    public TransactionDto get(@PathVariable int id, Principal principal) {
        int currentUserId = userDao.findIdByUsername(principal.getName());

        Transaction transaction = transDao.getTransaction(id);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Transaction");
        }
        if((transaction.getReceiverId()!=currentUserId||transaction.getSenderId()!=currentUserId) && !userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your transactions.");
        }

        return mapTransactionToDto(transaction);
    }

    @PostMapping
    public TransactionDto create(@RequestBody @Valid CreateTransactionDto dto, Principal principal) {

        int currentUserId = userDao.findIdByUsername(principal.getName());

        if(userDao.getUserById(dto.getTargetUserId())==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That user does not exist");
        }

        if(currentUserId == dto.getTargetUserId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send money to yourself");
        }

        Transaction transaction = null;
        if(dto.getType().equals("request")){
            transaction = new Transaction(1, dto.getAmount(), dto.getTargetUserId(), currentUserId, true, dto.getMemo(), "pending", LocalDateTime.now());
        } else if (dto.getType().equals("payment")){

            transaction = new Transaction(1, dto.getAmount(), currentUserId, dto.getTargetUserId(), false,
                    dto.getMemo(), "accepted", LocalDateTime.now());
            if (!canMakeWalletTransfer(walletDao.getWalletByUser(currentUserId).getId(), walletDao.getWalletByUser(dto.getTargetUserId()).getId(), dto.getAmount())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The wallet to transfer funds from has an " +
                        "insufficient balance to make payment.");
            }

            walletDao.transferBalance(walletDao.getWalletByUser(currentUserId).getId(), walletDao.getWalletByUser(dto.getTargetUserId()).getId(), dto.getAmount());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must be either \"request\" or \"payment\"");
        }

        Transaction newTransaction = transDao.createTransaction(transaction);
        return mapTransactionToDto(newTransaction);
    }

    @PatchMapping("/{id}")
    public TransactionDto confirmRequest(@PathVariable int id, @Valid @RequestBody TransactionStatusDto status) {

        Transaction transaction = transDao.getTransaction(id);

        //Check to see if Transaction exists
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Transaction");
        }

        //Check to see if Transaction is in valid status
        if (!transaction.getStatus().equals("pending")) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Cannot update a request that " +
                    "is not in the \"pending\" status.");
        }

        //Check to see if incoming DTO has a valid value in the status field
        if (!status.getStatus().equals("accepted") && !status.getStatus().equals("rejected")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for \"status\". " +
                    "Status must be either \"accepted\" or \"rejected\".");
        }

        transaction.setTransactionTime(LocalDateTime.now());

        if (status.getStatus().equals("accepted")) {
            //Get user Ids
            int senderId = transaction.getSenderId();
            int receiverId = transaction.getReceiverId();

            //Get wallet Ids for each User
            int senderWalletId = walletDao.getWalletByUser(senderId).getId();
            int receiverWalletId = walletDao.getWalletByUser(receiverId).getId();

            //Check to see if sending wallet has sufficient funds
            if (!canMakeWalletTransfer(senderWalletId, receiverWalletId, transaction.getAmount())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The wallet to transfer funds from has an " +
                        "insufficient balance to make payment.");
            }

            //Call DAO transfer balance method
            walletDao.transferBalance(senderWalletId, receiverWalletId, transaction.getAmount());

            //Update original transaction
            transaction.setStatus("accepted");
            transDao.updateTransaction(id, transaction);

        } else if (status.getStatus().equals("rejected")) {

            transaction.setStatus("rejected");
            transDao.updateTransaction(id, transaction);
        }

        return mapTransactionToDto(transDao.getTransaction(id));

    }


    private boolean canMakeWalletTransfer(int senderWalletId, int receiverWalletId, BigDecimal amount) {
        Wallet sender = walletDao.getWallet(senderWalletId);
        Wallet receiver = walletDao.getWallet(receiverWalletId);

        //If either wallet does not exists, throw an error
        if (sender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown wallet for Sending Wallet.");
        } else if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown wallet for Receiving Wallet.");
        }

        //Make sure sender has money in their account to make transfer
        return sender.getBalance().compareTo(amount) >= 0;
    }

    /*
    ########################################  Helper Methods  ##########################################
     */


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

    private Transaction mapDtoToTransaction(TransactionDto dto) {
        Transaction transaction = new Transaction();

        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setMemo(dto.getMemo());
        transaction.setReceiverId(dto.getReceiverId());
        transaction.setSenderId(dto.getSenderId());
        transaction.setRequest(dto.isRequest());
        transaction.setStatus(dto.getStatus());
        transaction.setTransactionTime(dto.getTransactionTime());

        return transaction;
    }

}
