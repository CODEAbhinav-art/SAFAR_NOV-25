package travel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Conn.java
 * Reusable database connection class for the Travel Management System.
 */
public class Conn {

    // Declare objects for connection and statement
    public Connection conn;
    public Statement stmt;

    // Constructor establishes the connection
    public Conn() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database credentials
            String url = "jdbc:mysql://localhost:3306/safardb";
            String user = "root";  // 🧠 change this to your MySQL username
            String pass = "root";  // 🔑 change this to your MySQL password

            // Create connection and statement
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();

            System.out.println("✅ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found: " + e);
        } catch (SQLException e) {
            System.out.println("❌ Database connection error: " + e);
        }
    }
}
