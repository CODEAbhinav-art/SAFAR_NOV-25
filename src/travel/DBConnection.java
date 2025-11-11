package travel;

import java.sql.*;

public class DBConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/safardb";
    private static final String USER = "root";        // change if needed
    private static final String PASSWORD = "root";        // your MySQL password

    // Returns a live connection to the database
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
