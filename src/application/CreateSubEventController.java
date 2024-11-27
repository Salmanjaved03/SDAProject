package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateSubEventController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField maxParticipantsField;
    @FXML
    private VBox subEventListVBox;

    @FXML
    private Button addEvent;
    @FXML
    private Button saveEvent;
    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> eventTypeComboBox;
    
    @FXML 
    private Button backButton;

    
    private List<SubEvents> subEvents;

    
    private int mainEventID;

    public CreateSubEventController() {
        subEvents = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        // Populate ComboBox options
        eventTypeComboBox.getItems().addAll("Individual", "Team");
        eventTypeComboBox.setValue("Individual"); // Default value
        
        
    }

   
    
   
    @FXML
    public void addSubEvent(ActionEvent event) {
    	
    	Event e1 = EventManager.getInstance().getSelectedEvent();
    	
		try {
			mainEventID = getMainEventIDByEventName(e1.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("id" + mainEventID);
    	
        String name = nameField.getText();
        String description = descriptionField.getText();
        String eventType = eventTypeComboBox.getValue();
        int maxParticipants;

        try {
            maxParticipants = Integer.parseInt(maxParticipantsField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for max participants.");
            return;
        }

        if (!name.isEmpty() && !description.isEmpty() && maxParticipants > 0) {
            SubEvents newSubEvent = new SubEvents(mainEventID, name, description, maxParticipants, eventType);
            subEvents.add(newSubEvent);

            subEventListVBox.getChildren().add(new Button("SubEvent: " + name + " (" + eventType + ")"));

            nameField.clear();
            descriptionField.clear();
            maxParticipantsField.clear();
        } else {
            System.out.println("Please fill in all fields correctly.");
        }
    }
    
    

   
    @FXML
    public void saveSubEvents(ActionEvent event) {
    	
    	
        for (SubEvents subEvent : subEvents) {
            try {
                int subEventID = subEvent.createSubEventInDatabase(mainEventID);
                System.out.println("SubEvent with ID " + subEventID + " created successfully.");
            } catch (SQLException e) {
                System.err.println("Error saving subevent: " + e.getMessage());
            }
        }
    }

    @FXML
    public void cancelCreation(ActionEvent event) {
        // Clear the input fields
        nameField.clear();
        descriptionField.clear();
        maxParticipantsField.clear();
        System.out.println("Creation canceled.");

        EventManager.getInstance().deleteEventAndSubEventsManual(mainEventID);
		

        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createEvent2.fxml"));
            Parent createEventRoot = loader.load();
            Scene createEventScene = new Scene(createEventRoot);
            

            // Switch to the Join Event scene
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(createEventScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Join Event scene.");
        }
    }

        
    //helping function
    public int getMainEventIDByEventName(String eventName) throws SQLException {
        int mainEventID = -1; // Default to -1 to indicate not found
        Connection connection = DatabaseHelper.getConnection();
        
        String query = "SELECT mainEventID FROM Events WHERE mainEventname = ?";
        
        try (Connection connection1 = DatabaseHelper.getConnection(); 
                PreparedStatement stmt = connection1.prepareStatement(query)) {
            stmt.setString(1, eventName); 
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                mainEventID = rs.getInt("mainEventID"); 
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving mainEventID by event name: " + e.getMessage());
        } finally {
            connection.close();
        }
        
        return mainEventID;
    }
    
    @FXML 
    private void handleBackButton(){
    	Venue selectedVenue = VenueManager.getInstance().getSelectedVenue();
    	selectedVenue.updateAvailability("true");
    	
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);

            String css = getClass().getResource("dash.css").toExternalForm();
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
