package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ssdb10000"; // Adjust database URL, username, and password
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123abc";

    
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

           
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }

        return connection;
    }


    // Method to execute an update (INSERT, UPDATE, DELETE)
    public static void executeUpdate(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();
        }
    }
    
    
    // Method to execute a query and retrieve results
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery();
    }
    
   
}
