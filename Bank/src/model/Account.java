package model;

public class Account {
    private String accountNumber;
    private int accountId;
    private int userId;
    private String accountType;
    private double balance;
    private double minimumBalance;
    private String status;

    public Account(int userId,String accountTypes,double balance,double minimumBalance,String status){
        this.userId = userId;
        this.accountType = accountTypes;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public Account(int  accountId, String accountNumber,int userId,String accountTypes,double balance,double minimumBalance,String status){
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountTypes;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.status = status;
        this.accountNumber = accountNumber;
    }

    public int getAccountId() {
        return accountId;
    }
    public int getUserId() {
        return userId;
    }
    public String getAccountType() {
        return accountType;
    }
    public double getBalance() {
        return balance;
    }
    public double getMinimumBalance() {
        return minimumBalance;
    }
    public String getAccountNumber() { return accountNumber;}
    public String getStatus() {
        return status;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", accountTypes='" + accountType + '\'' +
                ", balance=" + balance +
                ", minimumBalance=" + minimumBalance +
                ", status='" + status + '\'' +
                '}';
    }
}
