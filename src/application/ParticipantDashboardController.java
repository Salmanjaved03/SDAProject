package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Random;

public class ParticipantDashboardController {

    @FXML
    private Button joinEventButton;
    
    @FXML
    private Button myTeams;
    
    @FXML
    private Button notificationsButton;
    
    @FXML
    private Button rateEventButton;
    
   
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Label greetingLabel;
    
    @FXML
    private Label quoteLabel;
    
    // Array of sample sports quotes to display randomly
    private String[] sportsQuotes = {
        "Winning isn’t everything, but wanting to win is. – Vince Lombardi",
        "The only way to prove that you’re a good sport is to lose. – Ernie Banks",
        "Only he who can see the invisible can do the impossible. – Frank L. Gaines",
        "Champions keep playing until they get it right. – Billie Jean King",
        "You miss 100% of the shots you don’t take. – Wayne Gretzky",
        "The harder the battle, the sweeter the victory. – Les Brown"
    };

    // This method initializes the dashboard with a random sports quote
    @FXML
    private void initialize() {
        Random random = new Random();
        int randomIndex = random.nextInt(sportsQuotes.length);
        quoteLabel.setText(sportsQuotes[randomIndex]);
    }

    @FXML
    private void handleJoinEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewEvents.fxml"));
            Parent joinEventRoot = loader.load();
            Scene joinEventScene = new Scene(joinEventRoot);
            

            Stage stage = (Stage) joinEventButton.getScene().getWindow();
            stage.setScene(joinEventScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Join Event scene.");
        }
    }

    @FXML
    private void handleNotifications() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("notifications.fxml"));
            Parent notificationsRoot = loader.load();
            Scene notificationsScene = new Scene(notificationsRoot);

            
            Stage stage = (Stage) notificationsButton.getScene().getWindow();
            stage.setScene(notificationsScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Notifications scene.");
        }
    }

    @FXML
    private void handleRateEvent() {
        try {
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rateEvent.fxml"));
            Parent rateEventRoot = loader.load();
            Scene rateEventScene = new Scene(rateEventRoot);

            
            Stage stage = (Stage) rateEventButton.getScene().getWindow();
            stage.setScene(rateEventScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Rate An Event scene.");
        }
    }

    
    
    @FXML
    private void handleMyTeams() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myTeams.fxml"));
            Parent myTeamsRoot = loader.load();
            Scene myTeamsScene = new Scene(myTeamsRoot);

            
            Stage stage = (Stage) myTeams.getScene().getWindow();
            stage.setScene(myTeamsScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Create Team scene.");
        }
    }


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
    
    
    
}
