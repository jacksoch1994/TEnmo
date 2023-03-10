package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.LoginDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ConsoleService;
import org.apache.commons.logging.Log;

public class TenmoCLI {
    ConsoleService consoleService = new ConsoleService();
    UserDao userDao = new UserDao();
    LoginDao loginDao = new LoginDao();

    public static void main(String[] args) {
        TenmoCLI cli = new TenmoCLI();
        cli.run();
    }

    public void run(){
    }
}
