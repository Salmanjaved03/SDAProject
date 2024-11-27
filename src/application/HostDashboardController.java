package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Random;

public class HostDashboardController {

    @FXML
    private Button createEventButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button bookingButton; // New booking button
    
    @FXML
    private Label greetingLabel;
    
    @FXML
    private Label quoteLabel;
    
    @FXML
    private Button updateButton;

   
    private String[] sportsQuotes = {
        "Winning isn’t everything, but wanting to win is. – Vince Lombardi",
        "The only way to prove that you’re a good sport is to lose. – Ernie Banks",
        "Only he who can see the invisible can do the impossible. – Frank L. Gaines",
        "Champions keep playing until they get it right. – Billie Jean King",
        "You miss 100% of the shots you don’t take. – Wayne Gretzky",
        "The harder the battle, the sweeter the victory. – Les Brown"
    };

    // randomly initialize label with quotes
    @FXML
    private void initialize() {
        // Display a random sports quote
        Random random = new Random();
        int randomIndex = random.nextInt(sportsQuotes.length);
        quoteLabel.setText(sportsQuotes[randomIndex]);
    }

    
    @FXML
    private void handleNavigation(ActionEvent event) {
        try {
            String fxmlFile = "";

            
            if (event.getSource() == createEventButton) {
                fxmlFile = "bookVenue.fxml";
                if (!fxmlFile.isEmpty()) {
                	try {
                   	 FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                        Parent bookVenueRoot = loader.load();
                        Scene bookVenueScene = new Scene(bookVenueRoot);

                       
                      String css = getClass().getResource("bookvenue.css").toExternalForm();
                      bookVenueScene.getStylesheets().add(css);
                       
                        Stage stage = (Stage) createEventButton.getScene().getWindow();
                        stage.setScene(bookVenueScene);
                        stage.show();
                   } catch (Exception e) {
                   e.printStackTrace();
                   System.out.println("Error loading dashboard.");
                   }                            
                }
            } else if (event.getSource() == bookingButton) {
                handleBooking();
            }
            // Load the new scene
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    @FXML
    private void handleBooking() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("booking.fxml"));
            Parent bookingRoot = loader.load();
            Scene bookingScene = new Scene(bookingRoot);

            String css = getClass().getResource("booking.css").toExternalForm();
            bookingScene.getStylesheets().add(css);
            
            
            Stage stage = (Stage) bookingButton.getScene().getWindow();
            stage.setScene(bookingScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Booking scene.");
        }
    }

    // Handle logout
    @FXML
    private void handleLogout() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            String css = getClass().getResource("application.css").toExternalForm();
            loginScene.getStylesheets().add(css);
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Login scene.");
        }
    }
    
    @FXML
    private void handleUpdateButton() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteUpdateEvents.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            //String css = getClass().getResource("application.css").toExternalForm();
            //loginScene.getStylesheets().add(css);
            
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading deleteUpdate scene.");
        }
    }
}
