package Service;

import java.util.Scanner;

public class InputService {
    public static String getValidUserName(Scanner sc){
        while(true){
            String validUserName = sc.nextLine();
            if(validUserName.isEmpty()){
                System.out.println("User Name Cannot be Empty.");
                System.out.print("Re-Enter UserName : ");
                continue;
            }
            if(validUserName.length()<=4){
                System.out.println("User Name Length Should be atleast 4.");
                System.out.print("Re-Enter UserName : ");
                continue;
            }
            return validUserName;
        }
    }
    public static String getValidPassword(Scanner sc){
        while(true){
            String validPassword = sc.nextLine();
            if(validPassword.isEmpty()){
                System.out.println("Password Cannot be Empty.");
                System.out.print("Re-Enter Password : ");
                continue;
            }
            if(validPassword.length()<=4){
                System.out.println("Password Length Should be atleast 8.");
                System.out.print("Re-Enter Password : ");
                continue;
            }
            return validPassword;
        }
    }

    public static double getValidDeposit(Scanner sc, String accType) {
        while (true) {
            // 1. Check if the input is actually a number
            if (!sc.hasNextDouble()) {
                System.out.println("Deposit must be Numeric.");
                System.out.print("Re-Enter Deposit Amount: ");
                sc.next(); // Clear the invalid string from the buffer
                continue;
            }

            double validDeposit = sc.nextDouble();
            sc.nextLine(); // Consume newline

            // 2. Check for positive value (Note: fixed the > to <)
            if (validDeposit <= 0) {
                System.out.println("Deposit Cannot be zero or negative.");
                System.out.print("Re-Enter Deposit Amount: ");
                continue;
            }

            // 3. Check Account specific rules
            if (accType.equalsIgnoreCase("SAVINGS") && validDeposit < 1000) {
                System.out.println("Deposit Amount should be greater than 1000 for SAVINGS");
                System.out.print("Re-Enter Deposit Amount: ");
                continue;
            }

            return validDeposit;
        }
    }
    public static double getValidTxnAmount(Scanner sc) {
        while (true) {
            // 1. Check if the input is actually a number
            if (!sc.hasNextDouble()) {
                System.out.println("Amount must be Numeric.");
                System.out.print("Re-Enter Amount: ");
                sc.next(); // Clear the invalid string from the buffer
                continue;
            }

            double validTxnAmount = sc.nextDouble();
            sc.nextLine(); // Consume newline

            // 2. Check for positive value (Note: fixed the > to <)
            if (validTxnAmount <= 0) {
                System.out.println("Amount Cannot be zero or negative.");
                System.out.print("Re-Enter Amount: ");
                continue;
            }
            return validTxnAmount;
        }
    }
    public static String getValidUserChoice(Scanner sc){
        while(true){
            String validUserChoice = sc.nextLine();
            if(validUserChoice.isEmpty()){
                System.out.println("User Name Cannot be Empty.");
                System.out.print("Re-Enter Option : ");
                continue;
            }
            return validUserChoice;
        }
    }

    public static int getValidAccountId(Scanner sc){
        while(true){
            String validAccountId = sc.nextLine();
            if(validAccountId.isEmpty()){
                System.out.println("Account ID Cannot be Empty.");
                System.out.print("Re-Enter Account ID : ");
                continue;
            }
            return Integer.parseInt(validAccountId);
        }
    }
}
