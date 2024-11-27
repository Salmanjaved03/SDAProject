package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User {
    protected int userId;
    protected String username;
    protected String email;
    protected String password;
    private String role; 

    public User(int userId, String username, String email, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

   
   
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    
 
    
    public boolean saveToDatabase() {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, this.username);
            stmt.setString(2, this.password); 
            stmt.setString(3, this.role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }


        
    
}
