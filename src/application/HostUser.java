package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HostUser extends User {

    public HostUser(int userId, String username, String email, String password, String role) {
        super(userId, username, email, password,role);
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

    public void createEvent(Event e) {
       
        try {
            String query = "INSERT INTO events (eventID, name, description, date, location, maxParticipants, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            DatabaseHelper.executeUpdate(
                query,
                e.getEventID(),
                e.getName(),
                e.getDescription(),
                e.getDate(),
                e.getLocation(),
               // e.getMaxParticipants(),
                e.getStatus()
            );
            System.out.println("Event created successfully.");
        } catch (SQLException ex) {
            System.err.println("Error creating event: " + ex.getMessage());
        }
    }

    public void inviteFriends(int recipientId, int eventId) {
       
        try {
            String query = "INSERT INTO invitations (sender, recipient, eventID, status) VALUES (?, ?, ?, 'pending')";
            DatabaseHelper.executeUpdate(query, getUserId(), recipientId, eventId);
            System.out.println("Invitation sent successfully.");
        } catch (SQLException ex) {
            System.err.println("Error sending invitation: " + ex.getMessage());
        }
    }

    public void bookVenue(Venue v, Event e) {
      
        try {
            String query = "INSERT INTO bookings (userID, venueID, date, time) VALUES (?, ?, ?, ?)";
            DatabaseHelper.executeUpdate(
                query,
                getUserId(),
                v.getVenueID(),
                e.getDate()
                //e.getTime()
            );
            System.out.println("Venue booked successfully.");
        } catch (SQLException ex) {
            System.err.println("Error booking venue: " + ex.getMessage());
        }
    }

    public void updateVenueAvailability(int venueId, String newAvailability) {
        
        try {
            String query = "UPDATE venues SET availability = ? WHERE venueID = ?";
            DatabaseHelper.executeUpdate(query, newAvailability, venueId);
            System.out.println("Venue availability updated successfully.");
        } catch (SQLException ex) {
            System.err.println("Error updating venue availability: " + ex.getMessage());
        }
    }

	
    public HostUser loadUser(int userID) {
        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, userID);

            if (rs.next()) {
                this.userId = rs.getInt("userID");
                this.username = rs.getString("username");
                this.email = rs.getString("email");
                this.password = rs.getString("password");
                System.out.println("Host user loaded: " + this.username);
                return this; 
            } else {
                System.out.println("No Host user found with ID: " + userID);
                return null;
            }
        } catch (SQLException ex) {
            System.err.println("Error loading Host user: " + ex.getMessage());
            return null;
        }
    }

    
   

   
}
