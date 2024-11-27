package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventManager {
    
    private static EventManager instance;
    private Event selectedEvent;
    private int id;

    // Private constructor to enforce singleton pattern
    private EventManager() {}

    // Method to get the singleton instance of EventManager
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    // Method to set the selected event
    public void setSelectedEvent(Event event) {
        this.selectedEvent = event;
    }
    
    public void setSelectedEventID(int id) {
    	this.id = id;
    }
    
    public int getSelectedEventID() {
    	return id;
    }

    
    public Event getSelectedEvent() {
        return this.selectedEvent;
    }
    
    public int getMainEventID(Event event) {
        return event.getEventID();
    }
    
    public void deleteEventAndSubEventsManual(int mainEventId) {
        String deleteSubEventsQuery = "DELETE FROM subevents WHERE mainEventID = ?";
        String deleteMainEventQuery = "DELETE FROM events WHERE maineventID = ?";

        try {
            

            // First, delete sub-events
            try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                    PreparedStatement stmt = connection.prepareStatement(deleteSubEventsQuery)) {
                stmt.setInt(1, mainEventId);
                stmt.executeUpdate();
            }

            // Then, delete the main event
            try (Connection connection = DatabaseHelper.getConnection(); // Database connection utility
                    PreparedStatement stmt = connection.prepareStatement(deleteMainEventQuery)) {
                stmt.setInt(1, mainEventId);
                stmt.executeUpdate();
            }

            
            System.out.println("Main event and its sub-events deleted successfully.");
        } catch(SQLException e) {
        	e.printStackTrace();
        }

    
    }
}
