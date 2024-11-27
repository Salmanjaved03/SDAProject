package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UserNotificationsController {

    @FXML
    private Label userNameLabel;  

    @FXML
    private ListView<String> notificationListView;  

    @FXML
    private Button deleteButton;  

    @FXML
    private Button markAsReadButton; 
    
    @FXML
    private Button backButton;

    // This will initialize the data in the ListView when the scene is loaded
    @FXML
    private void initialize() {
        
        notificationListView.getItems().addAll("New message", "Event reminder", "Friend request");

        
        userNameLabel.setText("User Notifications");
    }

    
    @FXML
    private void handleDeleteNotification(MouseEvent event) {
        // Get the selected notification and delete it
        String selectedNotification = notificationListView.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {
            notificationListView.getItems().remove(selectedNotification);
        }
    }

   
    @FXML
    private void handleMarkAsRead(MouseEvent event) {
      
        String selectedNotification = notificationListView.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {
            System.out.println("Marked as read: " + selectedNotification);
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
