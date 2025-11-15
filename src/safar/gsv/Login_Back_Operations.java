package safar.gsv;

import java.sql.*;

public class Login_Back_Operations {
	
	public static boolean registerUser(String user_id, String password, String role) {
        String query = "INSERT INTO users (user_id, password, role) VALUES (?, ?, ?)";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user_id);
            ps.setString(2, password);
            ps.setString(3, role);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

   
    public static String loginUser(String userId, String password) {
        String query = "SELECT role FROM users WHERE user_id = ? AND password = ?";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        return null;
    }
	
	

}
