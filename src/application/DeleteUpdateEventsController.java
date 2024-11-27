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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteUpdateEventsController {
	
	private int userID;

    @FXML
    private ListView<Event> mainEventsListView; // Changed type to Event

    @FXML
    private Button deleteEventButton;

    @FXML
    private Button updateEventButton;

    // Observable list to hold Event objects
    private ObservableList<Event> mainEventsList;

    // Constructor
    public DeleteUpdateEventsController() {
        // Initialize the ObservableList
        mainEventsList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // Set the ListView items
        mainEventsListView.setItems(mainEventsList);

        // Load main events from the database
        loadMainEvents();

        // Customize the ListView to show event names
        mainEventsListView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null || event.getName() == null) {
                    setText(null);
                } else {
                    setText(event.getName());
                }
            }
        });
    }

    // Load main events created by the user
    private void loadMainEvents() {
        String query = "SELECT * FROM events WHERE userID = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            User currentUser = UserManager.getInstance().getCurrentUser();
            int userID = currentUser.getUserId();
            System.out.println("id :" + userID);
            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();
            mainEventsList.clear();

            while (rs.next()) {
                int mainEventID = rs.getInt("mainEventID");
                String eventName = rs.getString("maineventName");
                String eventDescription = rs.getString("description");
                Date date = rs.getDate("date");

                
                Event event = new Event(mainEventID, eventName, eventDescription, date, null);
                mainEventsList.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void handleDeleteEvent(ActionEvent event) {
    	
    	 
    	 
        Event selectedEvent = mainEventsListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(AlertType.WARNING, "No Event Selected", "Please select an event to delete.");
            return;
        }
        EventManager.getInstance().setSelectedEvent(selectedEvent);
        
        int mainID = selectedEvent.getEventID();
        
        EventManager.getInstance().deleteEventAndSubEventsManual(mainID);

       

        
       
        loadMainEvents(); 
    }

    
    @FXML
    private void handleUpdateEvent(ActionEvent event) {
        Event selectedEvent = mainEventsListView.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            showAlert(AlertType.WARNING, "No Event Selected", "Please select an event to update.");
            return;
        }

       
        EventManager.getInstance().setSelectedEvent(selectedEvent);
        System.out.println("id :" + selectedEvent.getEventID());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("subEventsUpdate.fxml"));
            Parent updateRoot = loader.load();
            
            Scene updateScene = new Scene(updateRoot);
            Stage stage = (Stage) updateEventButton.getScene().getWindow();
            stage.setScene(updateScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Create Team scene.");
        }
    }

   

    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

   
   
}
