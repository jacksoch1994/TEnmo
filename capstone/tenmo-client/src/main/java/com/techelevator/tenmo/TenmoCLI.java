package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.LoginDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ConsoleService;
import org.apache.commons.logging.Log;

public class TenmoCLI {
    ConsoleService consoleService = new ConsoleService();
    UserDao userDao = new UserDao();
    WalletDao walletDao = new WalletDao();
    LoginDao loginDao = new LoginDao();

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
        while (selection != 0) {
            consoleService.balanceDisplay(walletDao.getUserWallet().getBalance());
            consoleService.mainMenu();
            selection = consoleService.promptForSelection("Please select an item from the list above:");
            switch (selection) {
                case (1):
                    break;
                case (2):
                    break;
                case (3):
                    break;
                case (4):
                    break;
                case (5):
                    break;
                case (6):
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

}
