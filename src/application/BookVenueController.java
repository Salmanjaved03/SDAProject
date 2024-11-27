package application;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class BookVenueController {

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<String> venueListView; // Display venue names

    @FXML
    private Button confirmButton;

    @FXML
    private Button backButton;

    private List<Venue> venueList = new ArrayList<>(); // Store Venue objects
    private Venue selectedVenue = null; // Store selected Venue

    @FXML
    private void initialize() {
        System.out.println("Initializing BookVenueController...");
        if (venueListView == null) {
            System.err.println("Error: venueListView is not initialized!");
        } else {
            loadVenues(); // Populate the ListView with venues
        }

        
        venueListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        
        venueListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedVenue = venueList.stream()
                        .filter(venue -> venue.getName().equals(newValue))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    private void handleConfirmBooking(ActionEvent event) {
        if (selectedVenue == null) {
            showAlert(Alert.AlertType.WARNING, "No Venue Selected", "Please select a venue to book.");
            return;
        }
        
        VenueManager.getInstance().setSelectedVenue(selectedVenue);

        try {
            int venueID = selectedVenue.getVenueID();

            if (selectedVenue.checkAvailability(venueID)) {
                selectedVenue.bookVenue(venueID); // Book the venue
                showAlert(Alert.AlertType.INFORMATION, "Booking Successful", "Venue '" + selectedVenue.getName() + "' has been successfully booked.");
                loadCreateEventScene(); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Booking Failed", "The selected venue is not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while booking the venue.");
        }
    }

    private void loadVenues() {
        try {
            String query = "SELECT * FROM venues";
            ResultSet rs = DatabaseHelper.executeQuery(query); // Use your database helper to execute the query

            ObservableList<String> venueNames = FXCollections.observableArrayList();

            while (rs.next()) {
                int venueID = rs.getInt("venueID");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");

                
                Venue venue = new Venue(venueID, name, location, capacity);
                venueList.add(venue); 
                venueNames.add(name); 
            }

            venueListView.setItems(venueNames);
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error loading venues: " + ex.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load venues from the database.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Logic to navigate to the previous scene
        System.out.println("Back button clicked");
        // Example: load the previous scene using FXMLLoader
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

    private void loadCreateEventScene() {
        
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createEvent2.fxml"));
            Parent hostDashboardRoot = loader.load();
            Scene hostDashboardScene = new Scene(hostDashboardRoot);

           // String css = getClass().getResource("dash.css").toExternalForm();
           // hostDashboardScene.getStylesheets().add(css);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(hostDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Host Dashboard scene.");
        } 
    }
    
    public void cancelEventCreation() {
    	selectedVenue.updateAvailability("true");
    	System.out.println(selectedVenue.getName() + " is no longer booked");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
