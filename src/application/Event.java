package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private int eventID;
    private String name;
    private String description;
    private Date date;
    private String location;
    
    private List<ParticipantUser> participants;
    private String status;

   
    public Event(int eventID, String name, String description, Date date, String location) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
       // this.maxParticipants = maxParticipants;
        this.status = "open";
        this.participants = new ArrayList<>();
    }

    // Getters
    public int getEventID() { return eventID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
    public String getLocation() { return location; }
   // public int getMaxParticipants() { return maxParticipants; }
    public String getStatus() { return status; }
    public List<ParticipantUser> getParticipants() { return participants; }

    
    public void setStatus(String status) {
        if (status != null && !status.isEmpty()) {
            this.status = status; 
        } else {
            this.status = "Unknown"; 
        }
    }

   
    public void addParticipant(ParticipantUser user) {
        
            try {
                String query = "INSERT INTO create_event_participants (eventID, userID) VALUES (?, ?)";
                DatabaseHelper.executeUpdate(query, getEventID(), user.getUserId());
                participants.add(user);
                System.out.println("Participant added successfully.");
            } catch (SQLException ex) {
                System.err.println("Error adding participant: " + ex.getMessage());
            }
        
    }

    
    public void removeParticipant(ParticipantUser user) {
        try {
            String query = "DELETE FROM event_participants WHERE eventID = ? AND userID = ?";
            DatabaseHelper.executeUpdate(query, getEventID(), user.getUserId());
            participants.remove(user);
            System.out.println("Participant removed successfully.");
        } catch (SQLException ex) {
            System.err.println("Error removing participant: " + ex.getMessage());
        }
    }

    // Load participants from the database
    private void loadParticipants() {
        try {
            String query = "SELECT userID FROM event_participants WHERE eventID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, getEventID());

            participants.clear();
            while (rs.next()) {
                int userID = rs.getInt("userID");
                ParticipantUser participant = new ParticipantUser(userID, null, null, null, null);
                participants.add(participant);
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error loading participants: " + ex.getMessage());
        }
    }

    // Notify participants
    public void notifyParticipants(String message) {
        try {
            loadParticipants();
            for (ParticipantUser participant : participants) {
                String query = "INSERT INTO notifications (message, senderID, recipientID, status, timestamp) VALUES (?, ?, ?, ?, NOW())";
                DatabaseHelper.executeUpdate(query, message, null, participant.getUserId(), "unread");
            }
            System.out.println("Notifications sent to all participants.");
        } catch (SQLException ex) {
            System.err.println("Error notifying participants: " + ex.getMessage());
        }
    }

    // Load an event from the database
    public static Event loadEvent(int eventID) {
        Event event = null;
        try {
            String query = "SELECT eventID, name, description, date, location, maxParticipants, status FROM events WHERE eventID = ?";
            Connection connection = DatabaseHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, eventID);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                Date date = rs.getDate("date");
                String location = rs.getString("location");
                int maxParticipants = rs.getInt("maxParticipants");
                String status = rs.getString("status");

                event = new Event(eventID, name, description, date, location);
                event.setStatus(status);
            }
            rs.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error loading event from database: " + ex.getMessage());
        }
        return event;
    }

    public int createEventInDatabase(int userID) {
        String query = "INSERT INTO events (mainEventName, description, date, userID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, this.name);
            stmt.setString(2, this.description);
            stmt.setDate(3, this.date);
            stmt.setInt(4, userID);
            
            
            int result = stmt.executeUpdate();  
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;  
        }
    }

}
