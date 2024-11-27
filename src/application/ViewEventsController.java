package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewEventsController {

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, String> eventDateColumn;

    @FXML
    private Button selectEventButton;
    
    @FXML
    private Button backButton;

    private ObservableList<Event> eventsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the table
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Load events from the database
        loadEvents();
    }

    private void loadEvents() {
        try {
            String query = "SELECT * FROM events";  // Adjust the query based on your database
            ResultSet rs = DatabaseHelper.executeQuery(query);

            while (rs.next()) {
                int eventID = rs.getInt("maineventID");
                String name = rs.getString("mainEventName");
                String description = rs.getString("description");
                java.sql.Date date = rs.getDate("date");
                String location = rs.getString("location");
                

                Event event = new Event(eventID, name, description, date, location);
                eventsList.add(event);
            }

            eventsTable.setItems(eventsList);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectEvent() {
    	
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        
       

        if (selectedEvent != null) {
            EventManager.getInstance().setSelectedEvent(selectedEvent);
            System.out.println("succesful.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("viewSubEvents.fxml"));
                Parent subEventsRoot = loader.load();
                Scene subEventScene = new Scene(subEventsRoot);

               // String css = getClass().getResource("dash.css").toExternalForm();
               // dashboardScene.getStylesheets().add(css);

                Stage stage = (Stage) selectEventButton.getScene().getWindow();
                stage.setScene(subEventScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading view sub events.");
            }
        } else {
            // Show alert if no event is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event to join.");
            alert.showAndWait();
        }
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
