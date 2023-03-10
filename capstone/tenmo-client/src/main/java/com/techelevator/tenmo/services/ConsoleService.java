package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private Scanner in = new Scanner(System.in);

    public void banner(){
        System.out.println("-------------------------------------------");
    }

    public String usernameEnterScreen(){
        System.out.print("\nPlease enter your username: ");
        return in.nextLine();
    }

    public String passwordEnterScreen(){
        System.out.print("\nPlease enter your password: ");
        return in.nextLine();
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

    public void mainMenu() {
        System.out.println();
        System.out.println("----TEnmo Main Menu----");
        System.out.println("1: View Balance");
        System.out.println("2: Make Payment");
        System.out.println("3: Request Payment");
        System.out.println("4: View Pending Requests");
        System.out.println("5: Approve/Reject Pending Requests");
        System.out.println("6: View Transaction History");
        System.out.println("0: Logout");
        System.out.println();
    }

    public void loginMenu() {
        System.out.println();
        System.out.println("----Welcome to TEnmo!!----");
        System.out.println("1: Login");
        System.out.println("2: Register");
        System.out.println("0: Exit");
    }

    public int promptForSelection(String prompt) {
        System.out.print(prompt);
        int output;

        try {
            output = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            output = -1;
        }
        return output;
    }

    public void display(String message) {
        System.out.println(message);
    }
}
