package application;
import java.sql.*;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;
    
    @FXML
    private ComboBox<String> roleComboBox;

    
    
    @FXML
    public void initialize() {
        // Set items for ComboBox
        roleComboBox.getItems().addAll("Host", "Participant");
        roleComboBox.setValue("Host");  // Set default value
    }
   
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue(); 
        
        //check if password is correct or not
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");           
            return;
        }

        
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && selectedRole != null) {
            try (Connection conn = DatabaseHelper.getConnection()) {
                if (conn == null) {
                    System.out.println("Failed to connect to the database!");
                    return;
                }

                // SQL query to insert a new user into the database
                String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, username);  // Set the username
                    pstmt.setString(2, email);     // Set the email
                    pstmt.setString(3, password);  // Set the password
                    pstmt.setString(4, selectedRole); // Set the selected role (Host or Participant)

                    int affectedRows = pstmt.executeUpdate();  // Execute the query

                    if (affectedRows > 0) {
                        
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                long userId = generatedKeys.getLong(1);
                                System.out.println("Registration successful for " + username + " with ID: " + userId);

                             
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                                    Parent loginRoot = loader.load();
                                    Scene backToLoginScreen = new Scene(loginRoot);
                                    String css = this.getClass().getResource("application.css").toExternalForm();
                                    backToLoginScreen.getStylesheets().add(css);
                                    
                                    Stage stage = (Stage) usernameField.getScene().getWindow();
                                    stage.setScene(backToLoginScreen);
                                    stage.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Error loading login screen.");
                                }
                            } else {
                                System.out.println("Error: User ID not generated.");
                            }
                        }
                    } else {
                        System.out.println("Error: User registration failed.");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error during database operation.");
            }
        } else {
            System.out.println("Please fill in all fields and select a role.");
        }
    }



   
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent loginRoot = loader.load();
            Scene backToLoginScreen = new Scene(loginRoot);
            String css = this.getClass().getResource("application.css").toExternalForm();	
			
            backToLoginScreen.getStylesheets().add(css);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(backToLoginScreen);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading login screen.");
        }
    }
}

