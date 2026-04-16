package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/bank";
    private static final String username = "root";
    private static final String password = "0586";
    public static /*static*/ Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection(url,username,password);
            return cn;

        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Connection Failed");
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
