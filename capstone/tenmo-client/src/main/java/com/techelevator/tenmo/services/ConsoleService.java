package com.techelevator.tenmo.services;

import java.math.BigDecimal;

public class ConsoleService {

    public void banner(){
        System.out.println("-------------------------------------------");
    }

    public void usernameEnterScreen(){
        banner();
        System.out.println("Please enter your username");
    }

    public void passwordEnterScreen(){
        banner();
        System.out.println("Please enter your password");
    }

    public void balanceDisplay(BigDecimal balance){
        banner();
        System.out.printf("Your current account balance is: $%.2f", balance);
    }

    public void sendTEBucks(){
        banner();
        System.out.println("Users");
        System.out.println("ID ");
    }
}
