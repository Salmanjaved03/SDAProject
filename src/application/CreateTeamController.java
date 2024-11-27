package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateTeamController {

    @FXML
    private TextField playerNameField;

   
    @FXML
    private TextField playereEmailField;
    
    @FXML
    private TextField teamNameField;

    @FXML
    private Button addPlayerButton;

    @FXML
    private Button submitTeamButton;
    
   @FXML 
   private VBox playerList;
   

    private List<Player> players = new ArrayList<>();
    private static final int MINIMUM_PLAYERS = 2;  // Minimum number of participants required

    @FXML
    public void handleAddPlayer() {
        String playerName = playerNameField.getText();
        String playerEmail = playereEmailField.getText();

        if (playerName.isEmpty() || playerEmail.isEmpty()) {
            showAlert("Input Error", "Please fill in both Player Name and Position.");
            return;
        }

        // Add the player to the list
        Player newPlayer = new Player(playerName, playerEmail);
        players.add(newPlayer);
        
        System.out.println("plpayer added succesfully");

        // Create a label to display the player's name and email
        Label playerInfoLabel = new Label(playerName);

        // Optionally, you can style the label if you want
        //playerInfoLabel.setStyle("-fx-padding: 5; -fx-font-size: 14px;");

        // Add the label to the VBox
        playerList.getChildren().add(playerInfoLabel);

        // Clear input fields for the next player
        playerNameField.clear();
        playereEmailField.clear();
    }

    @FXML
    public void handleRegisterTeam() {
        String teamName = teamNameField.getText();

        if (teamName.isEmpty()) {
            showAlert("Input Error", "Please provide a team name.");
            return;
        }

        if (players.size() < MINIMUM_PLAYERS) {
            showAlert("Input Error", "A team must have at least " + MINIMUM_PLAYERS + " players.");
            return;
        }
        System.out.println("succesful");
        try (Connection connection = DatabaseHelper.getConnection()) {
            // Insert the team into the database and get the generated team ID
            String insertTeamQuery = "INSERT INTO teams (name, userid) VALUES (?, ?)";
            int userId = UserManager.getInstance().getCurrentUser().getUserId();
            try (PreparedStatement teamStatement = connection.prepareStatement(insertTeamQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                teamStatement.setString(1, teamName);
                teamStatement.setInt(2, userId);
                int affectedRows = teamStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating team failed, no rows affected.");
                }

                int teamId;
                try (var generatedKeys = teamStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        teamId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating team failed, no ID obtained.");
                    }
                }

                // Insert each player associated with this team into the database
                String insertPlayerQuery = "INSERT INTO create_team_participants (team_id, userid, player_name, player_email) VALUES (?, ?, ?, ?)";
                try (PreparedStatement playerStatement = connection.prepareStatement(insertPlayerQuery)) {
                    for (Player player : players) {
                        // Assuming user_id is fetched from the current user session or manager
                        

                        playerStatement.setInt(1, teamId);
                        playerStatement.setInt(2, userId);
                        playerStatement.setString(3, player.getName());
                        playerStatement.setString(4, player.getEmail());

                        playerStatement.addBatch();
                    }
                    playerStatement.executeBatch();
                }

                showAlert("Success", "Team and players registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to register the team.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class to store player information
    public static class Player {
        private final String name;
        private final String email;

        public Player(String name, String email) {
            this.name = name;
            this.email = email;
        }

       

		public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
