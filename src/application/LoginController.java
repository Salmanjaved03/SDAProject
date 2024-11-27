package application;


import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController {
	
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button forgotPasswordButton;

  
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the input fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter both username and password.");
            return;
        }

        // Database connection and authentication
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to connect to the database!");
                return;
            }

            
            String sql = "SELECT userID, username, email, password, role FROM users WHERE username = ? AND password = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // user details from the result set
                        int userID = rs.getInt("userID");
                        String fetchedUsername = rs.getString("username");
                        String email = rs.getString("email");
                        String fetchedPassword = rs.getString("password");
                        String role = rs.getString("role");

                       
                        System.out.println("Login successful!");

                        
                        if ("Host".equals(role)) {
                        	
                        	 // Create a User object
                            User authenticatedUser = new HostUser(userID, fetchedUsername, email, fetchedPassword, role);

                            // Store the authenticated user in UserManager
                            UserManager.getInstance().setCurrentUser(authenticatedUser);

                            
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
                                Parent dashboardRoot = loader.load();
                                Scene dashboardScene = new Scene(dashboardRoot);

                                String css = getClass().getResource("dash.css").toExternalForm();
                                dashboardScene.getStylesheets().add(css);

                                Stage stage = (Stage) loginButton.getScene().getWindow();
                                stage.setScene(dashboardScene);
                                stage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Error loading dashboard.");
                            }
                        } else if ("Participant".equals(role)) {
                        	
                        	 
                            User authenticatedUser = new ParticipantUser(userID, fetchedUsername, email, fetchedPassword, role);

                            // Storeauthenticated user in UserManager class
                            UserManager.getInstance().setCurrentUser(authenticatedUser);

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("participantDashboard.fxml"));
                                Parent dashboardRoot = loader.load();
                                Scene dashboardScene = new Scene(dashboardRoot);

                                String css = getClass().getResource("participantDashboard.css").toExternalForm();
                                dashboardScene.getStylesheets().add(css);

                                Stage stage = (Stage) loginButton.getScene().getWindow();
                                stage.setScene(dashboardScene);
                                stage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Error loading participant dashboard.");
                            }
                        } else {
                            System.out.println("Unknown role: " + role);
                            return;  // exit if role is unknow
                        }
                    } else {
                        System.out.println("Invalid credentials!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during database operation.");
        }
    }


   
    @FXML
    private void handleRegister() {
        System.out.println("Navigating to registration screen...");
        
        try {
            System.out.println("Navigating to registration screen...");
            
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Parent registrationRoot = loader.load();

           
            Scene regScene = new Scene(registrationRoot);

            
            String css = getClass().getResource("registration.css").toExternalForm();
            regScene.getStylesheets().add(css);

            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(regScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading registration screen.");
        }
    }

   
    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot password clicked!");
        
    }
}


