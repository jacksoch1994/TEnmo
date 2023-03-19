package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transaction;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    /*
    ########################################   Attributes   ##########################################
     */

    private Scanner in = new Scanner(System.in);

    /*
    ###########################################  Methods  ############################################
     */

    /**
     * Prints a line of hyphens to the screen.
     */
    public void banner(){
        System.out.println("-------------------------------------------");
    }

    /**
     * Prompts the user to enter a username, and returns the value.
     *
     * @return the username as a String
     */
    public String usernameEnterScreen(){
        System.out.print("\nPlease enter your username: ");
        return in.nextLine();
    }

    /**
     * Prompts the user to enter a password, and returns the value.
     *
     * @return the password as a String
     */
    public String passwordEnterScreen(){
        System.out.print("\nPlease enter your password: ");
        return in.nextLine();
    }

    /**
     * Displays an account balance to the console.
     *
     * @param balance an account balance as a BigDecimal
     */
    public void balanceDisplay(BigDecimal balance){
        banner();
        System.out.printf("Your current account balance is: $%.2f", balance);
    }

    /**
     * Displays the Main Menu.
     */
    public void mainMenu() {
        System.out.println();
        System.out.println("----TEnmo Main Menu----");
        System.out.println("1: View Balance");
        System.out.println("2: Make Payment");
        System.out.println("3: Request Payment");
        System.out.println("4: Approve/Reject Pending Requests");
        System.out.println("5: View Transaction History");
        System.out.println("0: Logout");
        System.out.println();
    }

    /**
     * Displays the Login Menu
     */
    public void loginMenu() {
        System.out.println();
        System.out.println("----Welcome to TEnmo!!----");
        System.out.println("1: Login");
        System.out.println("2: Register");
        System.out.println("0: Exit");
    }

    /**
     * Displays a prompt to the screen, and attempts to parse user input into an int. If the input cannot be parsed,
     * returns -1.
     *
     * @param prompt the prompt to display to the console.
     * @return the user input as an int. If user input cannot be parsed to an int, returns -1.
     */
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

    /**
     * Displays a prompt to the screen, and obtains user input.
     *
     * @param prompt the prompt to display to the console.
     * @return the user input as a String
     */
    public String promptForStringSelection(String prompt) {
        System.out.println();
        System.out.print(prompt);

        String output = in.nextLine();
        return output;
    }

    /**
     * Displays a prompt to the screen, and attempts to convert it to a BigDecimal.
     *
     * @param prompt the prompt to display to the console.
     * @return the User input as a BigDecimal. If the user input cannot be converted to a BigDecimal, returns zero.
     */
    public BigDecimal promptForMoneySelection(String prompt){
        System.out.println(prompt);

        String output = in.nextLine();

        try{
            BigDecimal bigDecimal = new BigDecimal(output);
            return bigDecimal;
        } catch (NumberFormatException e){
            System.out.println("String not converted to Big Decimal Correctly");
            return BigDecimal.ZERO;
        }
    }

    /**
     * Displays a message to the console.
     *
     * @param message the message to display to the console.
     */
    public void display(String message) {
        System.out.println(message);
    }

}
