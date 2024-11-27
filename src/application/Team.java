package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Team {
    private int teamID;
    private String name;
    private List<ParticipantUser> members;
    private String privacy;

    
    public Team(int teamID, String name, String privacy) {
        this.teamID = teamID;
        this.name = name;
        this.privacy = privacy;
    }
    
    public int getTeamID() {
    	return teamID;
    }
    public void addMember(ParticipantUser member) {
        try {
            String query = "INSERT INTO team_members (teamID, userID) VALUES (?, ?)";
            DatabaseHelper.executeUpdate(query, getTeamID(), member.getUserId());
            members.add(member); // Add member to local list
            System.out.println("Member added successfully.");
        } catch (SQLException ex) {
            System.err.println("Error adding member: " + ex.getMessage());
        }
    }

    
    public void removeMember(ParticipantUser member) {
        try {
            String query = "DELETE FROM team_members WHERE teamID = ? AND userID = ?";
            DatabaseHelper.executeUpdate(query, getTeamID(), member.getUserId());
            members.remove(member); // Remove member from local list
            System.out.println("Member removed successfully.");
        } catch (SQLException ex) {
            System.err.println("Error removing member: " + ex.getMessage());
        }
    }

    
    public void sendJoinRequest(ParticipantUser user) {
        try {
            String query = "INSERT INTO join_requests (teamID, userID, status) VALUES (?, ?, ?)";
            DatabaseHelper.executeUpdate(query, getTeamID(), user.getUserId(), "pending");
            System.out.println("Join request sent successfully.");
        } catch (SQLException ex) {
            System.err.println("Error sending join request: " + ex.getMessage());
        }
    }

    
    public void approveJoinRequest(int requestID, ParticipantUser user) {
        try {
            // Update the join request status to "approved"
            String updateRequestQuery = "UPDATE join_requests SET status = 'approved' WHERE requestID = ?";
            DatabaseHelper.executeUpdate(updateRequestQuery, requestID);

            // Add the user to the team if approved
            addMember(user);
            System.out.println("Join request approved.");
        } catch (SQLException ex) {
            System.err.println("Error approving join request: " + ex.getMessage());
        }
    }

    
    public void rejectJoinRequest(int requestID) {
        try {
            // Update the join request status to "rejected"
            String updateRequestQuery = "UPDATE join_requests SET status = 'rejected' WHERE requestID = ?";
            DatabaseHelper.executeUpdate(updateRequestQuery, requestID);

            System.out.println("Join request rejected.");
        } catch (SQLException ex) {
            System.err.println("Error rejecting join request: " + ex.getMessage());
        }
    }

    
    public void loadMembers() {
        try {
            String query = "SELECT userID FROM team_members WHERE teamID = ?";
            ResultSet rs = DatabaseHelper.executeQuery(query, getTeamID());

            members.clear(); // Clear current list
            while (rs.next()) {
                int userID = rs.getInt("userID");
                ParticipantUser member = new ParticipantUser(userID, null, null, null, null); // Simplified instantiation
                members.add(member);
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("Error loading members: " + ex.getMessage());
        }
    }
    
    
}
