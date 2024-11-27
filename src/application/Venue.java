package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Venue {
    private int venueID;
    private String name;
    private String location;
    private int capacity;
    private String availability;

    // Constructor
    public Venue(int venueID, String name, String location, int capacity) {
        this.venueID = venueID;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public int getVenueID() {
        return venueID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getAvailability() {
        return availability;
    }

    
    public boolean checkAvailability(int venueID) {
        try {
            String query = "SELECT availability FROM venues WHERE venueID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, venueID);

            if (rs.next()) {
                this.availability = rs.getString("availability");

                // Use .equals() for string comparison
                if ("true".equals(availability)) {
                    System.out.println("Venue with ID " + venueID + " is available.");
                    rs.close();
                    return true;
                } else {
                    System.out.println("Venue with ID " + venueID + " is NOT available.");
                    rs.close();
                    return false;
                }
            } else {
                System.out.println("No availability information found for venue ID: " + venueID);
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error checking venue availability: " + ex.getMessage());
        }
        return false; 
    }




  
    public void bookVenue(int venueID) {
        try {
            
            if (this.availability.equals("true")) {
                
                String query = "UPDATE venues SET availability = ? WHERE venueID = ?";
                DatabaseHelper.executeUpdate(query, "false", venueID);
                
                
                this.availability = "false";
                
                System.out.println("Venue '" + name + "' booked successfully. Updated availability: " + this.availability);
            } else {
                System.out.println("Venue '" + name + "' is not available for booking.");
            }
        } catch (SQLException ex) {
            System.err.println("Error booking venue: " + ex.getMessage());
        }
    }

    public void updateAvailability(String updatedAvailability) {
        try {
            
            if (updatedAvailability.equals("true") || updatedAvailability.equals("false")) {
              
                String query = "UPDATE venues SET availability = ? WHERE venueID = ?";
                DatabaseHelper.executeUpdate(query, updatedAvailability, venueID);

              
                this.availability = updatedAvailability;

                System.out.println("Availability for venue '" + name + "' updated to: " + availability);
            } else {
                System.out.println("Invalid availability value. Use 'true' or 'false'.");
            }
        } catch (SQLException ex) {
            System.err.println("Error updating venue availability: " + ex.getMessage());
        }
    }


    
    public Venue loadVenue(int venueID) {
        try {
            String query = "SELECT * FROM venues WHERE venueID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, venueID);

            if (rs.next()) {
                int id = rs.getInt("venueID");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");
                String availability = rs.getString("availability");

                rs.close();
                return new Venue(id, name, location, capacity);
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error loading venue: " + ex.getMessage());
        }
        return null;
    }

    public static List<Integer> getAvailableVenueIDs() {
        List<Integer> venueIDs = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT venueID FROM venues WHERE availability = 'true'";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int venueID = rs.getInt("venueID");
                    venueIDs.add(venueID);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving available venue IDs: " + ex.getMessage());
        }
        return venueIDs;
    }
   
   
    
}