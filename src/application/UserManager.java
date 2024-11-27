package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
   
    private static UserManager instance;
    
    
    private User currentUser;

   
    private UserManager() {}

    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

   
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    
    public User getCurrentUser() {
        return currentUser;
    }

    
    public void clearCurrentUser() {
        this.currentUser = null;
    }

    
    public User getUserFromDatabase(int userID) {
        String query = "SELECT userID, username, email, password, role FROM users WHERE userID = ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    // Create and return a User object
                    return new User(id, username, email, password, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching user from the database.");
        }
        return null;
    }
}
