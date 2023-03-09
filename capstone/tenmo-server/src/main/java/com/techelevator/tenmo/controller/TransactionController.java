package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionDto;
import com.techelevator.tenmo.model.TransactionStatusDto;
import com.techelevator.tenmo.model.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
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
    public List<TransactionDto> list(@RequestParam(required = false, name = "user-id") Integer userId ) {

        List<TransactionDto> response = new ArrayList<>();
        List<Transaction> transactions;

        //Check to see if UserId is provided
        if (userId != null) {
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
    public TransactionDto get(@PathVariable int id) {
        Transaction transaction = transDao.getTransaction(id);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Transaction");
        }
        return mapTransactionToDto(transaction);
    }

    @PostMapping
    public TransactionDto create(@RequestBody @Valid TransactionDto dto) {

        if (userDao.getUserById(dto.getReceiverId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown User for Receiver");
        } else if (userDao.getUserById(dto.getSenderId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown User for Sender");
        }

        Transaction createdTransaction = transDao.createTransaction(mapDtoToTransaction(dto));

        return mapTransactionToDto(createdTransaction);
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

        if (!status.getStatus().equals("accepted") && !status.getStatus().equals("rejected")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for \"status\". " +
                    "Status must be either \"accepted\" or \"rejected\".");
        }

        transaction.setTransactionTime(LocalDateTime.now());

        if (status.getStatus().equals("accepted")) {
            //Get user Ids
            int senderId = transaction.getSenderId();
            int receiverId = transaction.getReceiverId();


            int senderWalletId = walletDao.getWalletByUser(senderId).getId();
            int receiverWalletId = walletDao.getWalletByUser(receiverId).getId();

            walletDao.transferBalance(senderWalletId, receiverWalletId, transaction.getAmount());

            transaction.setStatus("accepted");
            transDao.updateTransaction(id, transaction);

        } else if (status.getStatus().equals("rejected")) {

            transaction.setStatus("rejected");
            transDao.updateTransaction(id, transaction);
        }

        return get(id);

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
