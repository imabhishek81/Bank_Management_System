package Dao;

import model.User;
import util.DBConnection;

import java.sql.*;

public class UserDAO {
    public boolean registerUser(User user){
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            int rows = ps.executeUpdate();
            return rows>0;
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists.");
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public User loginUser(String username, String password){
        String query = "SELECT user_id, username, password, role FROM users WHERE username = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");
                if (storedPassword.equals(password)) {
                    return new User(userId, username, role);
                }
                else {
                    return null;
                }
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        //return null;
    }
}
