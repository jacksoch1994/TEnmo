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

    /**
     * Run Tenmo CLI. Display initial Main Menu for login and process user input.
     */
    public void run() {

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

    /**
     * Display Main menu and process user input for selection.
     */
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

    /**
     * Prompt user for login information and if login is successful, route them to the main menu.
     */
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

    /**
     * Prompt user to register by creating a new account. Returns the user to the previous menu.
     */
    private void registerMenu() {
        String username = consoleService.usernameEnterScreen();
        String password = consoleService.passwordEnterScreen();

        if (loginDao.register(username, password)) {
            consoleService.display("New user successfully created! Please log in with your new credentials.");
        } else {
            consoleService.display("Failed to create new account. Returning to login menu.");
        }
    }

    /**
     * Menu to allow user to pay another user.
     */
    private void makePaymentMenu() {
        User[] users = userDao.findAll();

        //Display All Users
        for (User user : users) {
            String info = String.format("ID: %s   Name: %s", user.getId(), user.getUsername());
            consoleService.display(info);
        }

        int targetUserId = consoleService.promptForSelection("What is the ID of the person you want to pay?");

        //Validate user Id
        if (!isValidUserId(targetUserId)) {
            consoleService.display("Invalid User ID. Returning to previous menu.");
            return;
        }

        //Make sure user is not trying to pay themselves
        if (userDao.findOwnUser().getId() == targetUserId) {
            consoleService.display("Cannot make payment to self. Returning to previous menu.");
            return;
        }


        BigDecimal amount = consoleService.promptForMoneySelection("How much do you want to pay?");

        //Make sure amount provided by user is greater than zero.
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            consoleService.display("Invalid payment amount. Value must be greater than 0. Returning to previous menu.");
            return;
        }

        String memo = consoleService.promptForStringSelection("What memo (if any) do you want to give?");
        transactionDao.makePayment(targetUserId, amount, memo);
    }

    /**
     * Menu for user to request a payment from another user.
     */
    private void requestPaymentMenu() {

        User[] users = userDao.findAll();

        //Display All Users
        for (User user : users) {
            String info = String.format("ID: %s   Name: %s", user.getId(), user.getUsername());
            consoleService.display(info);
        }

        //Request User Id from User
        int targetUserId = consoleService.promptForSelection("What is the ID of the person you are requesting from?");

        //Validate user Id
        if (!isValidUserId(targetUserId)) {
            consoleService.display("Invalid User ID. Returning to previous menu.");
            return;
        }

        //Make sure user is not trying to pay themselves
        if (userDao.findOwnUser().getId() == targetUserId) {
            consoleService.display("Cannot make payment to self. Returning to previous menu.");
            return;
        }

        //Get payment amount
        BigDecimal amount = consoleService.promptForMoneySelection("How much do you want to request?");

        //Make sure amount provided by user is greater than zero.
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            consoleService.display("Invalid payment amount. Value must be greater than 0. Returning to previous menu.");
            return;
        }

        //Get user memo
        String memo = consoleService.promptForStringSelection("What memo (if any) do you want to give?");
        transactionDao.requestPayment(targetUserId, amount, memo);
    }


    /**
     * Menu allowing user to accept or reject requests awaiting their decision.
     */
    //choose whether to accept or reject a request sent to me
    private void acceptOrRejectPendingRequest() {
        Transaction[] transactions = viewPendingRequests();
        if (transactions.length > 0) {

            int transactionId = consoleService.promptForSelection("What transaction ID do you want to accept/reject?");
            if (isValidTransactionNumber(transactions, transactionId)) {

                boolean islooping = true;
                while (islooping) {

                    String newStatus = null;
                    //did it this way because it's probably easier for a user to understand
                    consoleService.display("");

                    int statusRequest = consoleService.promptForSelection("Please enter (1) to accept the request, (2) " +
                            "to reject the request, or (3) to return to the previous menu,");
                    if (statusRequest == 1) {
                        newStatus = "accepted";
                        transactionDao.acceptOrRejectPendingTransaction(transactionId, newStatus);
                        islooping = false;
                    } else if (statusRequest == 2) {
                        newStatus = "rejected";
                        transactionDao.acceptOrRejectPendingTransaction(transactionId, newStatus);
                        islooping = false;
                    } else if (statusRequest == 3) {
                        consoleService.display("Approval aborted. Returning to previous menu.");
                    } else {
                        System.out.println();
                        System.out.println("Invalid selection. Please enter a valid integer.");
                    }
                }

            } else {
                consoleService.display("Invalid transaction ID. Returning to previous menu.");
            }
        } else {
            System.out.println();
            System.out.println("You have no pending requests");
        }
    }

    /**
     * Helper method to print all transactions that the user has been a part of as either the sender or receiver.
     */
    private void viewTransactionHistory() {

        User me = userDao.findOwnUser();
        Transaction[] transactions = transactionDao.getTransactionHistory(me.getId());

        for (Transaction transaction : transactions) {

            User receiver = userDao.findUserById(transaction.getReceiverId());
            User sender = userDao.findUserById(transaction.getSenderId());

            String memo = (transaction.getMemo() == null || transaction.getMemo().length() < 1) ? "<No Memo>" : transaction.getMemo();
            String info = String.format("Transaction ID: %s   Transaction amount: $%.2f   Sender ID: %s   Sender Name: %s" +
                            "   Receiver ID: %s  Receiver Name: %s \nMemo: %s\n",
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getSenderId(),
                    sender.getUsername(),
                    transaction.getReceiverId(),
                    receiver.getUsername(),
                    memo);

            consoleService.display(info);
        }
    }

    /**
     * Helper method to obtain pending requests where the user is the recipient of the request.
     *
     * @return An array of Transactions containing all requests awaiting user decision.
     */
    private Transaction[] viewPendingRequests() {

        User me = userDao.findOwnUser();
        Transaction[] transactions = transactionDao.getOwnTransactions(me.getId());

        for (Transaction transaction : transactions) {

            if (transaction.getSenderId() == me.getId()) {

                User receiver = userDao.findUserById(transaction.getReceiverId());
                String memo = (transaction.getMemo() == null || transaction.getMemo().length() < 1) ? "<No Memo>" : transaction.getMemo();
                String info = String.format("Transaction ID: %s   Transaction amount: $%.2f   Requester ID: %s   Requester Name: %s  \nMemo: %s\n",
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getReceiverId(),
                        receiver.getUsername(),
                        memo);

                consoleService.display(info);
            }
        }
        return transactions;
    }

    /**
     * Helper method to determine if user specified transaction ID is in the list of Transaction provided.
     *
     * @param transactions the list of Transactions
     * @param option       the Transaction id to check
     * @return true if the Transaction id is in the list of Transaction, false otherwise
     */
    private boolean isValidTransactionNumber(Transaction[] transactions, int option) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == option) return true;
        }
        return false;
    }

    /**
     * Helper method to determine if the user specified user ID exists.
     *
     * @param userId the User ID to check
     * @return true if user exists, false otherwise
     */
    private boolean isValidUserId(int userId) {
        if (userDao.findUserById(userId) == null) {
            return false;
        } else {
            return true;
        }

    }


}
