package Dao;

import model.Account;
import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public boolean accountExists(int userId){
        String query = "SELECT account_id FROM accounts WHERE user_id = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAccount(Account account){
        String query = "INSERT INTO accounts (account_number,user_id,account_type, balance, minimum_balance, status)\n" +
                "VALUES (?, ? , ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            String accNumber = generateAccountNumber();

            ps.setString(1, accNumber);
            ps.setInt(2, account.getUserId());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getBalance());
            ps.setDouble(5, account.getMinimumBalance());
            ps.setString(6, "ACTIVE");
            int rows = ps.executeUpdate();
            return rows>0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccountByUserId(int userId){
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        try(Connection con  = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int accountId = rs.getInt("account_id");
                String accountNumber = rs.getString("account_number");
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                double minimumBalance = rs.getDouble("minimum_balance");
                String status = rs.getString("status");
                return new Account(accountId,accountNumber,userId, accountType, balance, minimumBalance, status);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public int getAccountIdByNumber(String accountNumber) {

        String query = "SELECT account_id FROM accounts WHERE account_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("account_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }
    public String getAccountNumberById(int accountId) {

        String query = "SELECT account_number FROM accounts WHERE account_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("account_number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    public boolean deposit(int accountId, double amount){
        String queryDeposit = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        String queryRecordTxnsData = "INSERT INTO transactions (transaction_id, account_id, related_account_id, amount, transaction_type)\n" +
                "VALUES (?, ?, NULL, ?, 'DEPOSIT')";
        Connection con = null;
        try{
            con = DBConnection.getConnection();

            con.setAutoCommit(false); //start txn

            PreparedStatement ps1 = con.prepareStatement(queryDeposit);
            PreparedStatement ps2 = con.prepareStatement(queryRecordTxnsData);

            //Update Balance
            ps1.setDouble(1,amount);
            ps1.setInt(2,accountId);
            int rows = ps1.executeUpdate();

            //Insert Txn Data Into Database
            long txnId = System.currentTimeMillis();

            ps2.setLong(1,txnId);
            ps2.setInt(2,accountId);
            ps2.setDouble(3,amount);

            ps2.executeUpdate();

            con.commit();
            ps1.close();
            ps2.close();
            return rows > 0;
        }
        catch (SQLException e){
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean withdraw(int accountId, double amount){
        String queryFetchBalance = "SELECT balance, minimum_balance FROM accounts WHERE account_id = ?";
        String queryWithdraw = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        String queryRecordTxnsData = "INSERT INTO transactions (transaction_id, account_id, related_account_id, amount, transaction_type)\n" +
                "VALUES (?, ?, NULL, ?, 'WITHDRAW')";
        Connection con = null;
        try{
            con = DBConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(queryFetchBalance);
            ps.setInt(1,accountId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                double minBalance = rs.getDouble("minimum_balance");
                double balance = rs.getDouble("balance");
                if(balance - amount >= minBalance){
                    PreparedStatement ps1 = con.prepareStatement(queryWithdraw);
                    PreparedStatement ps2 = con.prepareStatement(queryRecordTxnsData);

                    ps1.setDouble(1,amount);
                    ps1.setInt(2,accountId);
                    int rows = ps1.executeUpdate();

                    long txnId = System.currentTimeMillis();
                    ps2.setLong(1,txnId);
                    ps2.setInt(2,accountId);
                    ps2.setDouble(3,amount);

                    ps2.executeUpdate();

                    con.commit();
                    ps.close();
                    ps1.close();
                    ps2.close();
                    return rows > 0;
                }
            }
        }
        catch (SQLException e){
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean transfer(int fromAccountId, int toAccountId, double amount){
        if (fromAccountId == toAccountId || amount <= 0) {
            return false;
        }
        String querySender = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";

        String queryReceiver = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

        String queryTxn = "INSERT INTO transactions (transaction_id, account_id, related_account_id, amount, transaction_type)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        String queryFetchBalance = "SELECT balance, minimum_balance,account_type FROM accounts WHERE account_id = ?";
        Connection con = null;
        try{
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            PreparedStatement psCheck = con.prepareStatement(
                    "SELECT account_id FROM accounts WHERE account_id = ?");
            psCheck.setInt(1, toAccountId);
            ResultSet rsCheck = psCheck.executeQuery();

            if (!rsCheck.next()) {
                con.rollback();
                System.out.println("Receiver account not Found");
                return false;
            }

            PreparedStatement ps3 = con.prepareStatement(queryFetchBalance);
            ps3.setInt(1,fromAccountId);
            ResultSet rsSender =  ps3.executeQuery();
            if(!rsSender.next()){
                con.rollback();
                //System.out.println("Error inSender Balance Fetching");
                return false;
            }
            double balanceSender = rsSender.getDouble("balance");
            String accountType = rsSender.getString(("account_type"));
            double minBalance = rsSender.getDouble("minimum_balance");

            if(accountType.equals("SAVINGS") && balanceSender - amount < minBalance){
                con.rollback();
                return false;
            }

            //Deduct from sender
            PreparedStatement ps1 = con.prepareStatement(querySender);
            ps1.setDouble(1, amount);
            ps1.setInt(2, fromAccountId);
            if (ps1.executeUpdate() == 0){
                con.rollback();
                return false;
            }

            //Add to receiver
            PreparedStatement ps2 = con.prepareStatement(queryReceiver);
            ps2.setDouble(1, amount);
            ps2.setInt(2, toAccountId);
            if (ps2.executeUpdate() == 0){
                con.rollback();
                return false;
            }

            long txnId = System.currentTimeMillis();

            //Sender DEBIT
            PreparedStatement psTxn = con.prepareStatement(queryTxn);
            psTxn.setLong(1, txnId);
            psTxn.setInt(2, fromAccountId);
            psTxn.setInt(3, toAccountId);
            psTxn.setDouble(4, amount);
            psTxn.setString(5, "DEBIT");
            psTxn.executeUpdate();

            //Receiver CREDIT
            psTxn.setLong(1, txnId + 1);
            psTxn.setInt(2, toAccountId);
            psTxn.setInt(3, fromAccountId);
            psTxn.setDouble(4, amount);
            psTxn.setString(5, "CREDIT");
            psTxn.executeUpdate();

            con.commit();
            ps1.close();
            ps2.close();
            ps3.close();
            psCheck.close();
            psTxn.close();
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void getLastTransactions(int accountId) {

        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_id DESC LIMIT 25";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n---- Last 25 Transactions ----");

            while (rs.next()) {

                long txnId = rs.getLong("transaction_id");
                int relatedId = rs.getInt("related_account_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("transaction_type");

                String relatedAccNo = getAccountNumberById(relatedId);

                System.out.println("--------------------------------");
                if(type.equals("DEBIT")){
                    System.out.println("Sent ₹" + amount + " to " + relatedAccNo);
                }
                else if(type.equals("CREDIT")){
                    System.out.println("Received ₹" + amount + " from " + relatedAccNo);
                }
                else{
                    System.out.println("Txn ID : " + txnId);
                    System.out.println(type + " ₹" + amount);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String generateAccountNumber() {

        String date = java.time.LocalDate.now().toString().replace("-", "");

        String query = "SELECT COUNT(*) FROM accounts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return "ACC" + date + String.format("%03d", count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
