package application;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MyTeamsController {

    @FXML
    private ListView<String> teamsListView; 
    
    @FXML
    private Button myTeams;

    
    User user = UserManager.getInstance().getCurrentUser();
    int loggedInUserId = user.getUserId();
    
    @FXML
    private void handleMyTeams() {
        
        ArrayList<String> teams = getTeamsByUserId(loggedInUserId);

        if (teams.isEmpty()) {
            
            showAlert("No Teams Found", "You have not created any teams yet.");
        } else {
            
            teamsListView.getItems().setAll(teams);
        }
    }

    
    private ArrayList<String> getTeamsByUserId(int userId) {
        ArrayList<String> teams = new ArrayList<>();
        String query = "SELECT name FROM teams WHERE userId = ?";
        
        try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                teams.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while fetching your teams.");
        }
        return teams;
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
}
