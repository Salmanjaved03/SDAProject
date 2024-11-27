
package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Notification{
	private int NotificationID;
	private String message;
	private User sender;
	private User recipient;
	private String status;
	private Date timestamp;
	
	public Notification(int id, String message2, User sender2, User recipient2, String status2, Date timestamp2) {
		NotificationID = id;
		message = message2;
		sender = sender2;
		recipient = recipient2;
		status = status2;
		timestamp = timestamp2;
		
	}

	public int getNotificationID() {
        return NotificationID;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

 // Method to load notification data from the database
    public static Notification loadNotification(int notificationID) {
        try {
            String query = "SELECT * FROM notifications WHERE notificationID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, notificationID);

            if (rs.next()) {
                int id = rs.getInt("notificationID");
                String message = rs.getString("message");
                int senderID = rs.getInt("senderID");
                int recipientID = rs.getInt("recipientID");
                String status = rs.getString("status");
                Date timestamp = rs.getTimestamp("timestamp");

                HostUser senderUser = new HostUser(0, "", "", "", "");  
                User sender = senderUser.loadUser(senderID);  

                ParticipantUser recipientUser = new ParticipantUser(0, "", "", "", "");  
                User recipient = recipientUser.loadUser(recipientID);

                rs.close();
                return new Notification(id, message, sender, recipient, status, timestamp);
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error loading notification: " + ex.getMessage());
        }
        return null;
    }
    
    
    
    public void sendNotification() {
        try {
            // Insert the notification details into the database
            String query = "INSERT INTO notifications (message, senderID, recipientID, status, timestamp) VALUES (?, ?, ?, ?, ?)";
            DatabaseHelper.executeUpdate(query, message, sender.getUserId(), recipient.getUserId(), "unread", new java.sql.Timestamp(timestamp.getTime()));
            System.out.println("Notification sent successfully.");
        } catch (SQLException ex) {
            System.err.println("Error sending notification: " + ex.getMessage());
        }
    }

    
    public void markAsRead() {
        try {
            // Update the notification status to 'read'
            String query = "UPDATE notifications SET status = 'read' WHERE notificationID = ?";
            DatabaseHelper.executeUpdate(query, getNotificationID());
            this.status = "read"; // Update the local status
            System.out.println("Notification marked as read.");
        } catch (SQLException ex) {
            System.err.println("Error marking notification as read: " + ex.getMessage());
        }
    }

    
	
	
}
  
 