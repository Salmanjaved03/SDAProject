package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinEventController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> eventListView;

    @FXML
    private ListView<String> subEventListView;

    @FXML
    private Button joinButton;

    private ObservableList<String> eventList = FXCollections.observableArrayList();
    private ObservableList<String> subEventList = FXCollections.observableArrayList();

    private ParticipantUser currentUser; 
    private Event selectedEvent; 
    private String selectedSubEvent; 

    
    public void setCurrentUser(ParticipantUser user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        loadEvents();
        
        eventListView.setOnMouseClicked(this::handleEventSelection);
        subEventListView.setOnMouseClicked(this::handleSubEventSelection);
    }

    
    private void loadEvents() {
        try {
            String query = "SELECT eventID, name FROM events WHERE status = 'open'";
            ResultSet rs = DatabaseHelper.executeQuery(query);

            eventList.clear();
            while (rs.next()) {
                int eventID = rs.getInt("eventID");
                String eventName = rs.getString("name");
                eventList.add(eventID + " - " + eventName);
            }
            rs.close();
            eventListView.setItems(eventList);
        } catch (SQLException e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
    }

    
    private void handleEventSelection(MouseEvent event) {
        String selectedItem = eventListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int eventID = Integer.parseInt(selectedItem.split(" - ")[0]);
            selectedEvent = Event.loadEvent(eventID);

            if (selectedEvent != null) {
                loadSubEvents();
            } else {
                System.out.println("Event not found.");
            }
        }
    }

    
    private void loadSubEvents() {
        try {
            String query = "SELECT subEventName FROM sub_events WHERE mainEventID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, selectedEvent.getEventID());

            subEventList.clear();
            while (rs.next()) {
                String subEventName = rs.getString("subEventName");
                subEventList.add(subEventName);
            }
            rs.close();
            subEventListView.setItems(subEventList);
        } catch (SQLException e) {
            System.err.println("Error loading sub-events: " + e.getMessage());
        }
    }

    private void handleSubEventSelection(MouseEvent event) {
        selectedSubEvent = subEventListView.getSelectionModel().getSelectedItem();
        if (selectedSubEvent != null) {
            System.out.println("Selected sub-event: " + selectedSubEvent);
        }
    }

   
    @FXML
    private void handleJoinEvent() {
        if (selectedEvent == null || selectedSubEvent == null) {
            System.out.println("Please select an event and a sub-event.");
            return;
        }

        try {
            String query = "SELECT type FROM sub_events WHERE mainEventID = ? AND subEventName = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, selectedEvent.getEventID(), selectedSubEvent);

            if (rs.next()) {
                String type = rs.getString("type");

                if (type.equalsIgnoreCase("team")) {
                  
                    System.out.println("Sub-event is of type 'team'. Proceed to team creation.");
                   
                } else if (type.equalsIgnoreCase("individual")) {
                    
                    currentUser.joinEvent(selectedEvent);
                } else {
                    System.out.println("Unknown sub-event type.");
                }
            } else {
                System.out.println("Sub-event not found.");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error handling join event: " + e.getMessage());
        }
    }

    // Search for events by name
    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            loadEvents(); // Reload all events if search is empty
            return;
        }

        try {
            String query = "SELECT eventID, name FROM events WHERE status = 'open' AND name LIKE ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, "%" + searchQuery + "%");

            eventList.clear();
            while (rs.next()) {
                int eventID = rs.getInt("eventID");
                String eventName = rs.getString("name");
                eventList.add(eventID + " - " + eventName);
            }
            rs.close();
            eventListView.setItems(eventList);
        } catch (SQLException e) {
            System.err.println("Error searching events: " + e.getMessage());
        }
    }
}
