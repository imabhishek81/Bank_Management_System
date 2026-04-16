package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    public User(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public User(int userId, String username, String role){
        this.username = username;
        this.userId = userId;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getRole(){
        return role;
    }
    public int getUserId(){
        return userId;
    }
}
