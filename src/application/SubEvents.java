package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubEvents {

    private int subEventID;  // Auto-generated
    private int mainEventID;
    private String name;
    private String description;
    private int maxParticipants;
    private String type;

    // Constructor (Updated: Does not require subEventID)
    public SubEvents(int mainEventID, String name, String description, int maxParticipants, String type) {
        this.mainEventID = mainEventID;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.type = type;
    }

    
    public int createSubEventInDatabase(int mainEventID) throws SQLException {
        int newSubEventID = 0;
        Connection connection = DatabaseHelper.getConnection();
        System.out.println("Attempting to create subevent...");

        
        String insertQuery = "INSERT INTO SubEvents (MainEventID, Name, Description, MaxParticipants, Eventtype) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, mainEventID);     
            statement.setString(2, this.name);    
            statement.setString(3, this.description);  
            statement.setInt(4, this.maxParticipants);  
            statement.setString(5, this.type);     

            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the generated SubEventID
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    newSubEventID = rs.getInt(1);  // Get the auto-generated SubEventID
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Error creating subevent in the database: " + e.getMessage());
        } finally {
            connection.close();
        }
        return newSubEventID;  // Return the newly created SubEventID
    }

    
    public boolean joinIndividualUser(User user, int subEventID) {
      
    	
    	
    	
        try {
            if (getCurrentParticipantCount() >= this.maxParticipants) {
                System.out.println("Cannot join, the sub-event has reached its maximum number of participants.");
                return false;  
            }
            
            
            Connection connection = DatabaseHelper.getConnection();
            
            
            
            String insertQuery = "INSERT INTO participants (subEventID, userID) VALUES (?, ?) ";
            
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            	
                statement.setInt(1, subEventID);  
                statement.setInt(2, user.getUserId()); 
                
                int rowsInserted = statement.executeUpdate();
                
                if (rowsInserted > 0) {
                    System.out.println("User successfully joined the sub-event!");
                    return true;  
                } else {
                    System.out.println("Failed to join the sub-event.");
                    return false;  
                }
            }
        } catch (SQLException e) {
            System.out.println("Error joining sub-event: " + e.getMessage());
            return false;  
        }
    }
    private int getCurrentParticipantCount() throws SQLException {
        int count = 0;
        Connection connection = DatabaseHelper.getConnection();
        String query = "SELECT COUNT(*) FROM participants WHERE subEventID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.subEventID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving participant count: " + e.getMessage());
            throw new SQLException("Error retrieving participant count: " + e.getMessage());
        }
        return count;
    }
    
    
    
    public String getType() {
    	return type;
    }

    // Getters and setters if needed
    public int getSubEventID() {
        return subEventID;
    }

    public int getMainEventID() {
        return mainEventID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

	public Object getSubEventName() {
		// TODO Auto-generated method stub
		return name;
	}
}
