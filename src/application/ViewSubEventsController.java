package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewSubEventsController {
	
	public int subEventID;

    // FXML fields corresponding to the FXML elements
    @FXML
    private TableView<SubEvents> subEventsTable;

    @FXML
    private TableColumn<SubEvents, String> subEventNameColumn;

    @FXML
    private TableColumn<SubEvents, String> subEventTypeColumn;

    @FXML
    private Button selectSubEventButton;
    
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Initialize table columns with a lambda expression
        subEventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        subEventTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        // Load sub-events from the database
        loadSubEvents();
    }


    private void loadSubEvents() {
        // Retrieve the selected event from EventManager (assumed to be previously selected)
        Event selectedEvent = EventManager.getInstance().getSelectedEvent();
        
        if (selectedEvent != null) {
            int selectedEventID = selectedEvent.getEventID(); // Unique identifier for the event
            
            // Fetch sub-events for the selected event from the database
            List<SubEvents> subEvents = getSubEventsFromDatabase(selectedEventID);
            subEventsTable.getItems().setAll(subEvents); // Set the items in the table
        }
    }

    private List<SubEvents> getSubEventsFromDatabase(int eventID) {
        List<SubEvents> subEvents = new ArrayList<>();
        
        String query = "SELECT SubEventID, Name, Description, MaxParticipants, Eventtype FROM SubEvents WHERE MainEventID = ?";
        
        try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, eventID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subEventID = resultSet.getInt("SubEventID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    int maxParticipants = resultSet.getInt("MaxParticipants");
                    String eventType = resultSet.getString("Eventtype");
                    
                    // Create a new SubEvent object
                    SubEvents subEvent = new SubEvents(subEventID, name, description, maxParticipants, eventType);
                    
                    // Add the sub-event to the list
                    subEvents.add(subEvent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving sub-events from the database.");
        }
        
        return subEvents;
    }

    @FXML
    private void handleSelectSubEvent() {
        SubEvents selectedSubEvent = subEventsTable.getSelectionModel().getSelectedItem();

        if (selectedSubEvent != null) {
            // Handle the selection of the sub-event
            if ("Individual".equalsIgnoreCase(selectedSubEvent.getType())) {
                handleIndividualSubEvent(selectedSubEvent);
            } else if ("Team".equalsIgnoreCase(selectedSubEvent.getType())) {
                handleTeamSubEvent();
            } else {
                showAlert("Unsupported Sub-Event Type", "The selected sub-event type is not recognized.");
            }
        } else {
            showAlert("No Sub-Event Selected", "Please select a sub-event to join.");
        }
    }

    private void handleIndividualSubEvent(SubEvents subEvent) {
        // Join an individual sub-event
        boolean joinedSuccessfully = subEvent.joinIndividualUser(UserManager.getInstance().getCurrentUser(), subEventID);

        if (joinedSuccessfully) {
            showAlert("Success", "User joined successfully!");
        } else {
            showAlert("Error", "Unable to join the sub-event.");
        }
    }

    private void handleTeamSubEvent() {
        // Handle joining a team-based sub-event
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createTeam.fxml"));
            Parent createTeamRoot = loader.load();
            
            Scene createTeamScene = new Scene(createTeamRoot);
            Stage stage = (Stage) selectSubEventButton.getScene().getWindow();
            stage.setScene(createTeamScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Create Team scene.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML 
    private void handleBackButton(){
    	
    	
    	
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewEvents.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);

           // String css = getClass().getResource("participantDashboard.css").toExternalForm();
            //dashboardScene.getStylesheets().add(css);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading participant dashboard.");
        }
   
    }
}
