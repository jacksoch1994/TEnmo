package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.LoginDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ConsoleService;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TenmoCLI {
    ConsoleService consoleService = new ConsoleService();
    UserDao userDao = new UserDao();
    WalletDao walletDao = new WalletDao();
    LoginDao loginDao = new LoginDao();
    TransactionDao transactionDao = new TransactionDao();

    public static void main(String[] args) {
        TenmoCLI cli = new TenmoCLI();
        cli.run();
    }

    public void run(){

        int selection = -1;
        while (selection != 0) {
            consoleService.loginMenu();
            selection = consoleService.promptForSelection("Please select an item from the list above:");
            switch (selection) {
                case (1):
                    loginMenu();
                    break;
                case (2):
                    registerMenu();
                    break;
                case (0):
                    break;
                default:
                    consoleService.display("Invalid selection.");
                    break;
            }
        }

    }

    private void mainMenu() {
        int selection = -1;
        consoleService.balanceDisplay(walletDao.getUserWallet().getBalance());
        while (selection != 0) {
            consoleService.mainMenu();
            selection = consoleService.promptForSelection("Please select an item from the list above:");
            switch (selection) {
                case (1):
                    consoleService.balanceDisplay(walletDao.getUserWallet().getBalance());
                    break;
                case (2):
                    makePaymentMenu();
                    break;
                case (3):
                    requestPaymentMenu();
                    break;
                case (4):
                    acceptOrRejectPendingRequest();
                    break;
//                case (5):
//                    acceptOrRejectPendingRequest();
//                    break;
                case (5):
                    viewTransactionHistory();
                    break;
                case (0):
                    break;
                default:
                    consoleService.display("Invalid selection.");
                    break;
            }
        }
    }

    private void loginMenu() {
        String username = consoleService.usernameEnterScreen();
        String password = consoleService.passwordEnterScreen();
        String token = loginDao.login(username, password);

        if (token != null) {
            userDao.setAuthToken(token);
            walletDao.setAuthToken(token);
            transactionDao.setAuthToken(token);
            mainMenu();
        } else {
            consoleService.display("Invalid Credentials. Returning to Login Screen.");
        }
    }

    private void registerMenu() {
        String username = consoleService.usernameEnterScreen();
        String password = consoleService.passwordEnterScreen();

        if (loginDao.register(username, password)) {
            consoleService.display("New user successfully created! Please log in with your new credentials.");
        } else {
            consoleService.display("Failed to create new account. Returning to login menu.");
        }
    }

    private void makePaymentMenu(){
        User[] users = userDao.findAll();
        for(User user: users){
            System.out.printf("ID: %s    Name: %s\n", user.getId(), user.getUsername());
        }
        int targetUserId = consoleService.promptForSelection("What is the ID of the person you want to pay?");
        BigDecimal amount = consoleService.promptForMoneySelection("How much do you want to pay?");
        String memo = consoleService.promptForStringSelection("What memo (if any) do you want to give?");
        transactionDao.makePayment(targetUserId, amount, memo);
    }

    private void requestPaymentMenu(){
        User[] users = userDao.findAll();
        for(User user: users){
            System.out.printf("ID: %s   Name: %s\n", user.getId(), user.getUsername());
        }
        int targetUserId = consoleService.promptForSelection("What is the ID of the person you are requesting from?");
        BigDecimal amount = consoleService.promptForMoneySelection("How much do you want to request?");
        String memo = consoleService.promptForStringSelection("What memo (if any) do you want to give?");
        transactionDao.requestPayment(targetUserId, amount, memo);
    }

    //view only requests for money sent to me
    private Transaction[] viewPendingRequests(){
        User me = userDao.findOwnUser();
        Transaction[] transactions = transactionDao.getOwnTransactions(me.getId());
        for(Transaction transaction: transactions){
            if (transaction.getSenderId()==me.getId()){
                int id = transaction.getId();
                BigDecimal amount = transaction.getAmount();
                int senderId = transaction.getSenderId();
                String memo = transaction.getMemo();
                System.out.printf("Transaction ID: %s   Transaction amount: $%.2f   Requester ID: %s    Memo: %s\n",id,amount,senderId,memo);
            }
        }
        return transactions;
    }

    //choose whether to accept or reject a request sent to me
    private void acceptOrRejectPendingRequest(){
        Transaction[] transactions= viewPendingRequests();
        if(transactions.length>0) {
            int transactionId = consoleService.promptForSelection("What transaction ID do you want to accept/reject?");
            boolean islooping = true;
            while (islooping) {
                String newStatus = null;
                //did it this way because it's probably easier for a user to understand
                System.out.println();
                int statusRequest = consoleService.promptForSelection("Do you want to accept(1) or reject(2) the request?");
                if (statusRequest == 1) {
                    newStatus = "accepted";
                    transactionDao.acceptOrRejectPendingTransaction(transactionId, newStatus);
                    islooping = false;
                } else if (statusRequest == 2) {
                    newStatus = "rejected";
                    transactionDao.acceptOrRejectPendingTransaction(transactionId, newStatus);
                    islooping = false;
                } else {
                    System.out.println();
                    System.out.println("Please enter (1) for accept or (2) for reject");
                }
            }
        } else {
            System.out.println();
            System.out.println("You have no pending requests");
        }
    }

    private void viewTransactionHistory(){
        User me = userDao.findOwnUser();
        Transaction[] transactions = transactionDao.getTransactionHistory(me.getId());
        for(Transaction transaction: transactions){
            int id = transaction.getId();
            BigDecimal amount = transaction.getAmount();
            int senderId = transaction.getSenderId();
            int receiverId = transaction.getReceiverId();
            String memo = transaction.getMemo();
            System.out.printf("Transaction ID: %s   Transaction amount: $%.2f   Sender ID: %s    ReceiverID: %s    Memo: %s\n",id,amount,senderId,receiverId,memo);
        }
    }


}
