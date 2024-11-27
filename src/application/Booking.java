package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Booking {
	
	
	private int bookingID;
	private HostUser user;
	private Venue venue;
	private Date date;
	private Date time;
	
	
	public Booking(int bookingID, HostUser user, Venue venue, Date date, Date time) {
		this.bookingID = bookingID;
		this.user = user;
		this.venue = venue;
		this.date = date;
		this.time = time;
		
	}
	
	 public int getBookingID() {
	        return bookingID;
	    }

	    public HostUser getUser() {
	        return user;
	    }

	    public Venue getVenue() {
	        return venue;
	    }

	    public Date getDate() {
	        return date;
	    }

	    public Date getTime() {
	        return time;
	    }

	    // Method to book the venue
		/*
		 * public void bookVenue() { try { // Check if venue is available (availability
		 * is now a boolean check) if (venue.checkAvailability()) { // Insert booking
		 * record into database String query =
		 * "INSERT INTO bookings (userID, venueID, date, time) VALUES (?, ?, ?, ?)";
		 * 
		 * // Assuming you are passing the correct date and time from your form or input
		 * DatabaseHelper.executeUpdate(query, user.getUserId(), venue.getVenueID(),
		 * date, time);
		 * 
		 * // Optionally, mark the venue as unavailable after booking (update the
		 * availability) venue.updateAvailability("false");
		 * 
		 * System.out.println("Venue successfully booked!"); } else {
		 * System.out.println("Venue is not available at the selected time."); } } catch
		 * (SQLException ex) { System.err.println("Error booking venue: " +
		 * ex.getMessage()); } }
		 */

	    // Method to cancel the booking
	    public void cancelBooking() {
	        try {
	            // SQL query to delete booking record from database
	            String query = "DELETE FROM bookings WHERE bookingID = ?";
	            DatabaseHelper.executeUpdate(query, bookingID);

	            System.out.println("Booking canceled successfully.");
	        } catch (SQLException ex) {
	            System.err.println("Error canceling booking: " + ex.getMessage());
	        }
	    }

	    // Method to view booking details
	    public static Booking viewBookingDetails(int bookingID) {
	        try {
	            // SQL query to retrieve booking details based on bookingID
	            String query = "SELECT * FROM bookings WHERE bookingID = ?";
	            ResultSet rs = DatabaseHelper.executeQuery(query, bookingID);

	            if (rs.next()) {
	                // Retrieve details from result set
	                int userID = rs.getInt("userID");
	                int venueID = rs.getInt("venueID");
	                Date date = rs.getDate("date");
	                Date time = rs.getTime("time");

	                // Load related HostUser and Venue objects based on IDs
	                HostUser hostusers = new HostUser(0, "", "", "","");
	                HostUser user = hostusers.loadUser(userID);  // Assuming loadUser() exists
	                Venue v = new Venue(0, "", "", 0);
	                Venue venue = v.loadVenue(venueID);  // Assuming loadVenue() exists

	                rs.close();
	                return new Booking(bookingID, user, venue, date, time);
	            }
	            rs.close();
	        } catch (SQLException ex) {
	            System.err.println("Error retrieving booking details: " + ex.getMessage());
	        }
	        return null;  // If no booking found
	    }

}
