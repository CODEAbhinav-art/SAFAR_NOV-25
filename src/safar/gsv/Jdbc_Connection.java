package safar.gsv;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Jdbc_Connection {

   
    private static final String url = "jdbc:mysql://localhost:3306/safar";
    private static final String user = "root";       
    private static final String password = "root";

    private static Connection con = null;

    
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            // System.out.println("Database Connected Successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        return con;
    }

    
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Connection Closed Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
