package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubEventsViewController {
	
	int mainEventiD;
	
	

    @FXML
    private ListView<SubEvents> subEventsListView; 

    @FXML
    private Button addSubEventButton;

    @FXML
    private Button deleteSubEventButton;

    
    private ObservableList<SubEvents> subEventsList;

    private Event mainEvent;

    
    @FXML
    private void initialize() {
        subEventsList = FXCollections.observableArrayList();
        subEventsListView.setItems(subEventsList);
        subEventsListView.setCellFactory(param -> new javafx.scene.control.ListCell<SubEvents>() {
            @Override
            protected void updateItem(SubEvents subEvent, boolean empty) {
                super.updateItem(subEvent, empty);
                if (empty || subEvent == null || subEvent.getSubEventName() == null) {
                    setText(null);
                    setTextFill(Color.BLACK); // Set default text color (black)
                } else {
                    setText((String) subEvent.getSubEventName());
                    // Example: Set text color based on the sub-event name
                    setTextFill(getColorForSubEvent((String) subEvent.getSubEventName()));
                }
            }
        });

        // Load sub-events of the main event
        loadSubEvents();
    }

    
    private Color getColorForSubEvent(String subEventName) {
       
        switch (subEventName) {
            case "Special Event":
                return Color.RED;
            case "Completed Event":
                return Color.GREEN;
            case "Canceled Event":
                return Color.GRAY;
            default:
                return Color.BLACK; // Default color
        }
    }


    
    
    private void loadSubEvents() {
        mainEvent = EventManager.getInstance().getSelectedEvent();
 
        if (mainEvent == null) return;

        String query = "SELECT * FROM subevents WHERE mainEventID = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, mainEvent.getEventID());

            ResultSet rs = stmt.executeQuery();
            subEventsList.clear();

            while (rs.next()) {
                int subEventID = rs.getInt("subEventID");
                String subEventName = rs.getString("name");
                String subEventDescription = rs.getString("description");
                String type = rs.getString("eventType");
                int max_participants = rs.getInt("maxparticipants");

               
                SubEvents subEvent = new SubEvents(mainEvent.getEventID(), subEventName, subEventDescription, max_participants,  type);
                subEventsList.add(subEvent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void handleAddSubEvent(ActionEvent event) {
    	
    	System.out.println("succcesful.");
    	 
        if (mainEvent == null) {
            showAlert(AlertType.WARNING, "No Main Event Selected", "Please select a main event before adding sub-events.");
            return;
        }

        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createSubEvents.fxml"));
            Parent joinEventRoot = loader.load();
            Scene joinEventScene = new Scene(joinEventRoot);
            

            // Switch to the Join Event scene
            Stage stage = (Stage) addSubEventButton.getScene().getWindow();
            stage.setScene(joinEventScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Join Event scene.");
        }
    }

    // this method handles deleting sub event 
    @FXML
    private void handleDeleteSubEvent(ActionEvent event) {
        SubEvents selectedSubEvent = subEventsListView.getSelectionModel().getSelectedItem();

        if (selectedSubEvent == null) {
            showAlert(AlertType.WARNING, "No Sub-Event Selected", "Please select a sub-event to delete.");
            return;
        }

        deleteSubEventFromDatabase(selectedSubEvent);
        loadSubEvents(); // Refresh the sub-events list
    }

    //delete from database
    private void deleteSubEventFromDatabase(SubEvents subEvent) {
    	
        String deleteSubEventQuery = "DELETE FROM subevents WHERE subEventID = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(deleteSubEventQuery)) {

            stmt.setInt(1, subEvent.getSubEventID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
   
}
