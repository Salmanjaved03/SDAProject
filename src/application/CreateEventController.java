package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class CreateEventController {

    @FXML
    private TextField eventNameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField maxParticipantsField;

    @FXML
    private DatePicker eventDateField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitButton;
    
    BookVenueController bvc;

    /**
     * Action triggered when the "Create" button is clicked.
     * Validates input, creates an event, and saves it in the database.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    private void submitButtonAction(ActionEvent event) {
        String eventName = eventNameField.getText();
        String description = descriptionField.getText();
        LocalDate eventDate = eventDateField.getValue(); // Get date from DatePicker
        String maxParticipantsStr = maxParticipantsField.getText();

        
            if (eventName.isEmpty() || description.isEmpty() || eventDate == null || maxParticipantsStr.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled out.");
            }

            
            int maxParticipants = Integer.parseInt(maxParticipantsStr);

            
            if (maxParticipants <= 0) {
                throw new IllegalArgumentException("Max participants must be greater than 0.");
            }
            
            

            // Create a new Event instance
            Event newEvent = new Event(0, eventName, description, java.sql.Date.valueOf(eventDate), null);

            // Save the event in the database
            int result = 0;
			
            User user = UserManager.getInstance().getCurrentUser();
            int userID = user.getUserId();
			result = newEvent.createEventInDatabase(userID);
			
			EventManager.getInstance().setSelectedEvent(newEvent);
			
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Event created successfully!");
                // Redirect back to another scene if needed
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("createSubEvents.fxml"));
                    Parent subEventsRoot = loader.load();
                    Scene subEventsScene = new Scene(subEventsRoot);

                  //  String css = getClass().getResource("dash.css").toExternalForm();
                  //  hostDashboardScene.getStylesheets().add(css);

                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.setScene(subEventsScene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error loading Host subEvents scene.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create the event. Please try again.");
            }
        
    }

    /**
     * Action triggered when the "Cancel" button is clicked.
     * Returns to the previous scene or performs a desired action.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
    	
    	Venue selectedVenue = VenueManager.getInstance().getSelectedVenue();
    	selectedVenue.updateAvailability("true");
    	
    	try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bookVenue.fxml"));
            Parent hostDashboardRoot = loader.load();
            Scene hostDashboardScene = new Scene(hostDashboardRoot);

            String css = getClass().getResource("bookVenue.css").toExternalForm();
            hostDashboardScene.getStylesheets().add(css);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(hostDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Host Dashboard scene.");
        }
    }

    /**
     * Displays an alert to the user.
     *
     * @param alertType the type of alert (e.g., INFORMATION, ERROR, WARNING)
     * @param title     the title of the alert dialog
     * @param message   the message content of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
