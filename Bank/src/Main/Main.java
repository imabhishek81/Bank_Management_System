package Main;
import Dao.AccountDAO;
import Dao.UserDAO;
import Service.InputService;
import model.Account;
import model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("1.Register");
            System.out.println("2.Login");
            System.out.println("3.Exit");
            System.out.print("Choose Above Options : ");
            String chooice = InputService.getValidUserChoice(sc);
            switch(chooice){
                case "1":
                    while(true){
                        System.out.print("Enter UserName : ");
                        String username = InputService.getValidUserName(sc);
                        System.out.print("Eneter Passward : ");
                        String passward = InputService.getValidPassword(sc);
                        String role = "CUSTOMER";

                        User user = new User(username,passward,role);
                        UserDAO userDAO = new UserDAO();

                        if(!userDAO.registerUser(user)){
                            System.out.println("Regestration Failed");
                            continue;
                        }
                        else{
                            System.out.println("Regestration Succefully done");
                            break;
                        }
                    }
                    break;
                case "2":
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.loginUser(username, password);

                    if (user == null) {
                        System.out.println("Invalid credentials");
                        break;
                    }

                    System.out.println("Login Successful");
                    System.out.println(user);
                    AccountDAO accountDAO = new AccountDAO();

                    if (!accountDAO.accountExists(user.getUserId())) {
                        System.out.println("1. Create Savings Account");
                        System.out.println("2. Create Salary Account");

                        System.out.print("Choose Above Options : ");
                        String typeChoice = InputService.getValidUserChoice(sc);
                        switch (typeChoice) {
                            case "1":
                                System.out.println("Chosen Savings Account");
                                System.out.print("Enter Deposit Amount : ");

                                double depositAmount = InputService.getValidDeposit(sc, "SAVINGS");

                                Account acc1 = new Account(user.getUserId(), "SAVINGS", depositAmount, 1000, "ACTIVE");

                                if (accountDAO.createAccount(acc1)) {
                                    System.out.println("Savings Account Created Successfully");
                                } else {
                                    System.out.println("Failed to create account");
                                }
                                break;

                            case "2":
                                System.out.println("Chosen Salary Account");
                                System.out.print("Enter Deposit Amount : ");

                                double salaryDeposit = InputService.getValidDeposit(sc, "SALARY");

                                Account acc2 = new Account(user.getUserId(), "SALARY", salaryDeposit, 0, "ACTIVE");

                                if (accountDAO.createAccount(acc2)) {
                                    System.out.println("Salary Account Created Successfully");
                                } else {
                                    System.out.println("Failed to create account");
                                }
                                break;
                            default:
                                System.out.println("Invalid choice");
                                continue;
                        }
                    }
                    else {
                        System.out.println("Account already exists.");
                        while(true){
                            System.out.println("---- Banking Menu ---- ");
                            System.out.println("1.Deposit");
                            System.out.println("2.Withdraw");
                            System.out.println("3.Send Money");
                            System.out.println("4.Check Balance");
                            System.out.println("5.Transaction History");
                            System.out.println("6.Logout");
                            System.out.print("Choose Above Options : ");
                            String txnChoice = InputService.getValidUserChoice(sc);
                            switch (txnChoice) {
                                case "1":
                                    Account accObjDeposit = accountDAO.getAccountByUserId(user.getUserId());
                                    System.out.print("Enter Amount To Deposit : ");
                                    double depositAmt = InputService.getValidTxnAmount(sc);
                                    if(!accountDAO.deposit(accObjDeposit.getAccountId(),depositAmt)){
                                        System.out.println("Deposit Failed");
                                        break;
                                    }
                                    else{
                                        System.out.println("Deposit Succefully Done");
                                    }
                                    break;
                                case "2":
                                    Account accObjWithdraw = accountDAO.getAccountByUserId(user.getUserId());
                                    System.out.print("Enter Amount To Withdraw : ");
                                    double withdrawAmt = InputService.getValidTxnAmount(sc);
                                    if(!accountDAO.withdraw(accObjWithdraw.getAccountId(),withdrawAmt)){
                                        System.out.println("Withdraw Failed (Exceeed Withdraw Amt or DB Error)");
                                        break;
                                    }else{
                                        System.out.println("Withdraw Succefully Done");
                                    }
                                    break;
                                case "3":
                                    Account accObjTxn = accountDAO.getAccountByUserId(user.getUserId());
                                    System.out.print("Enter Amount To Transfer : ");
                                    double transferAmt = InputService.getValidTxnAmount(sc);
                                    System.out.print("Enter Receiver Account Number: ");
                                    String receiverAccNumber = sc.nextLine();

                                    int receiverAccountId = accountDAO.getAccountIdByNumber(receiverAccNumber);

                                    if (receiverAccountId == -1) {
                                        System.out.println("Invalid Account Number");
                                        break;
                                    }
                                    if(!accountDAO.transfer(accObjTxn.getAccountId(),receiverAccountId,transferAmt)){
                                        System.out.println("Transaction Failed");
                                        break;
                                    }else{
                                        System.out.println("Transaction Succefully Done");
                                    }
                                    break;
                                case "4":
                                    Account accObjFetchBal = accountDAO.getAccountByUserId(user.getUserId());
                                    System.out.println("Current Balance : "+accObjFetchBal.getBalance());
                                    break;
                                case "5":
                                    Account accObjHistory = accountDAO.getAccountByUserId(user.getUserId());
                                    accountDAO.getLastTransactions(accObjHistory.getAccountId());
                                    break;
                                case "6":
                                    System.out.println("Logging out...");
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                            }
                            if (txnChoice.equals("6")) break;
                        }
                    }
                    break;
                case "3":
                    System.out.println("Exiting .....");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
