package application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantUser extends User {

    
    public ParticipantUser(int userID, String username, String email, String password, String role) {
        super(userID, username, email, password, role);
    }

  
	 public void resetPassword() {
	        
	        try {
	            String newPassword = "newSecurePassword"; 
	            String query = "UPDATE users SET password = ? WHERE userID = ?";
	            DatabaseHelper.executeUpdate(query, newPassword, getUserId());
	            System.out.println("Password reset successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error resetting password: " + e.getMessage());
	        }
	    }

	    public void joinEvent(Event e) {
	        
	        try {
	            String query = "INSERT INTO event_participants (eventID, userID) VALUES (?, ?)";
	            DatabaseHelper.executeUpdate(query, e.getEventID(), getUserId());
	            System.out.println("Successfully joined the event: " + e.getName());
	        } catch (SQLException ex) {
	            System.err.println("Error joining event: " + ex.getMessage());
	        }
	    }

	    public void rateEvent(Event e, int score, String comments) {
	       
	        try {
	            String query = "INSERT INTO ratings (eventID, userID, score, comments) VALUES (?, ?, ?, ?)";
	            DatabaseHelper.executeUpdate(query, e.getEventID(), getUserId(), score, comments);
	            System.out.println("Event rated successfully.");
	        } catch (SQLException ex) {
	            System.err.println("Error rating event: " + ex.getMessage());
	        }
	    }

	    public void trackParticipation() {
	        // Method to track participation by retrieving events the user is participating in
	        try {
	            String query = "SELECT e.eventID, e.name, e.date, e.location FROM events e " +
	                           "JOIN event_participants ep ON e.eventID = ep.eventID WHERE ep.userID = ?";
	            ResultSet rs = DatabaseHelper.executeQuery(query, getUserId());

	            System.out.println("Participation Details:");
	            while (rs.next()) {
	                System.out.println("Event ID: " + rs.getString("eventID"));
	                System.out.println("Event Name: " + rs.getString("name"));
	                System.out.println("Event Date: " + rs.getDate("date"));
	                System.out.println("Event Location: " + rs.getString("location"));
	                System.out.println("------------");
	            }
	            rs.close();
	        } catch (SQLException ex) {
	            System.err.println("Error tracking participation: " + ex.getMessage());
	        }
	    }

	    public void viewEventDetails(Event e) {
	        // Method to view details of a specific event
	        try {
	            String query = "SELECT * FROM events WHERE eventID = ?";
	            ResultSet rs = DatabaseHelper.executeQuery(query, e.getEventID());

	            if (rs.next()) {
	                System.out.println("Event Details:");
	                System.out.println("Event ID: " + rs.getString("eventID"));
	                System.out.println("Name: " + rs.getString("name"));
	                System.out.println("Description: " + rs.getString("description"));
	                System.out.println("Date: " + rs.getDate("date"));
	                System.out.println("Location: " + rs.getString("location"));
	                System.out.println("Max Participants: " + rs.getInt("maxParticipants"));
	                System.out.println("Status: " + rs.getString("status"));
	            } else {
	                System.out.println("Event not found.");
	            }
	            rs.close();
	        } catch (SQLException ex) {
	            System.err.println("Error viewing event details: " + ex.getMessage());
	        }
	    }

	    public void giveFeedback(Event e, int rating, String comments) {
	        // Method to provide feedback for an event
	        try {
	            String query = "INSERT INTO feedback (eventID, userID, rating, comments) VALUES (?, ?, ?, ?)";
	            DatabaseHelper.executeUpdate(query, e.getEventID(), getUserId(), rating, comments);
	            System.out.println("Feedback submitted successfully.");
	        } catch (SQLException ex) {
	            System.err.println("Error giving feedback: " + ex.getMessage());
	        }
	    }
	    
	    public ParticipantUser loadUser(int userID) {
	        try {
	            String query = "SELECT * FROM users WHERE userID = ?";
	            ResultSet rs = DatabaseHelper.executeQuery(query, userID);

	            if (rs.next()) {
	                this.userId = rs.getInt("userID");
	                this.username = rs.getString("username");
	                this.email = rs.getString("email");
	                this.password = rs.getString("password");
	                System.out.println("Participant user loaded: " + this.username);
	                return this; // Return the ParticipantUser object
	            } else {
	                System.out.println("No Participant user found with ID: " + userID);
	                return null;
	            }
	        } catch (SQLException ex) {
	            System.err.println("Error loading Participant user: " + ex.getMessage());
	            return null;
	        }
	    }
	
}