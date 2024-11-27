package application;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BookingController {

    @FXML
    private ComboBox<String> venueComboBox; 
    @FXML
    private DatePicker datePicker; 
    @FXML
    private Button bookButton; 
    @FXML
    private Button backButton; 
    @FXML
    private Label bookingStatusLabel; 
    
    
    private Venue venue;
    // Initialize the booking scene with available venuesSSSS
    @FXML
    private void initialize() {
        loadAvailableVenues();
    }
    
    private void loadAvailableVenues() {
        List<Integer> venueIDs = Venue.getAvailableVenueIDs(); 
        if (venueIDs == null || venueIDs.isEmpty()) {
            bookingStatusLabel.setText("No available venues found.");
            return;
        }

        
        for (int venueID : venueIDs) {
            Venue venue = new Venue(venueID, null, null, venueID); 
            venue = venue.loadVenue(venueID);

            if (venue != null) {
                venueComboBox.getItems().add(venue.getName()); 
            }
        }
    }

    @FXML
    private void handleBooking() {
        String selectedVenue = venueComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedVenue == null || selectedDate == null) {
            showAlert(AlertType.WARNING, "Booking Error", "Please select a venue and a date.");
            return;
        }

        // Insert booking details into the database
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn == null) {
                bookingStatusLabel.setText("Failed to connect to the database!");
                return;
            }

            String sql = "UPDATE venues SET availability = 'false' WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedVenue);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    bookingStatusLabel.setText("Booking successful for " + selectedVenue + " on " + selectedDate);
                } else {
                    bookingStatusLabel.setText("Failed to book the venue.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            bookingStatusLabel.setText("Error during booking.");
        }
    }

    // Go back to the previous Host Dashboard scene
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
            Parent hostDashboardRoot = loader.load();
            Scene hostDashboardScene = new Scene(hostDashboardRoot);

            String css = getClass().getResource("dash.css").toExternalForm();
            hostDashboardScene.getStylesheets().add(css);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(hostDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Host Dashboard scene.");
        }
    }

    // Show alert message
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
