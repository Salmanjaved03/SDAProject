package application;

import java.sql.*;

public class Rating {
    private int ratingID;
    private int userID;
    private int eventID;
    private int score;
    private String comments;

    // Constructor
    public Rating(int ratingID2, int userID, int eventID, int score, String comments) {
        this.ratingID = ratingID2;
        this.userID = userID;
        this.eventID = eventID;
        this.score = score;
        this.comments = comments;
    }

    // Getters
    public int getRatingID() {
        return ratingID;
    }

    public int getUserID() {
        return userID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    // Method to submit a rating to the database
    public boolean submitRating(Connection connection) {
        String query = "INSERT INTO ratings (ratingID, userID, eventID, score, comments) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ratingID);
            stmt.setInt(2, userID);
            stmt.setInt(3, eventID);
            stmt.setInt(4, score);
            stmt.setString(5, comments);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rating submitted successfully.");
                return true;
            } else {
                System.out.println("Failed to submit rating.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error submitting rating: " + e.getMessage());
            return false;
        }
    }

    // Method to view a rating from the database
    public static Rating viewRating(Connection connection, int ratingID) {
        String query = "SELECT * FROM ratings WHERE ratingID = ?";
        Rating rating = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ratingID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userID = rs.getInt("userID");
                int eventID = rs.getInt("eventID");
                int score = rs.getInt("score");
                String comments = rs.getString("comments");

                rating = new Rating(ratingID, userID, eventID, score, comments);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rating: " + e.getMessage());
        }

        return rating;
    }
}
