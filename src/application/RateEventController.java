package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RateEventController {

    @FXML
    private ListView<String> eventListView;
    @FXML
    private HBox starsHBox;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private Button submitRatingButton;
    
    @FXML
    private Button backButton;
   
    private int selectedRating = 0;

    @FXML
    public void initialize() {
        loadEvents();
        setupStars();
    }

    private void loadEvents() {
        ArrayList<String> events = new ArrayList<>();
        String query = "SELECT mainEventName FROM events";

        try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(rs.getString("mainEventname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while fetching events.");
        }

        eventListView.getItems().setAll(events);
    }

    private void setupStars() {
        ToggleButton[] starButtons = new ToggleButton[5];
        for (int i = 0; i < starsHBox.getChildren().size(); i++) {
            starButtons[i] = (ToggleButton) starsHBox.getChildren().get(i);
            int starValue = i + 1;
            starButtons[i].setOnAction(event -> {
                selectedRating = starValue;
                updateStarSelection(starButtons, starValue);
            });
        }
    }

    private void updateStarSelection(ToggleButton[] starButtons, int starValue) {
        for (int i = 0; i < starButtons.length; i++) {
            starButtons[i].setSelected(i < starValue);
        }
    }

    @FXML
    private void handleSubmitRating() {
        String selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null || selectedRating == 0) {
            showAlert("Incomplete Rating", "Please select an event and a star rating.");
            return;
        }

        String comments = commentsTextArea.getText();

        // Save the rating to the database
        String query = "INSERT INTO ratings (userID, eventID, stars, comments) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                PreparedStatement stmt = connection.prepareStatement(query)) {
            // Assume userID is available through session or user context
            int userID = getCurrentUserID();
            int eventID = getEventIDByName(selectedEvent);

            stmt.setInt(1, userID);
            stmt.setInt(2, eventID);
            stmt.setInt(3, selectedRating);
            stmt.setString(4, comments);
            stmt.executeUpdate();

            showAlert("Success", "Your rating has been submitted!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while submitting your rating.");
        }
    }

    private int getCurrentUserID() {
        // Replace with the actual method to get the logged-in user's ID
        return UserManager.getInstance().getCurrentUser().getUserId();
    }

    private int getEventIDByName(String eventName) {
        String query = "SELECT maineventID FROM events WHERE mainEventname = ?";
        try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                PreparedStatement stmt
                = connection.prepareStatement(query)) {
            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("maineventID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // If event not found
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML 
    private void handleBackButton(){
    	
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("participantDashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);

            String css = getClass().getResource("participantDashboard.css").toExternalForm();
            dashboardScene.getStylesheets().add(css);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading participant dashboard.");
        }
   
    }

   
}
