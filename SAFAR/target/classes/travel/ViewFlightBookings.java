package travel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * ViewFlightBookings.java
 * -----------------------
 * Displays all flight bookings made by the logged-in user.
 * Data is fetched from `flight_bookings` and `flights` tables.
 */
public class ViewFlightBookings extends JFrame {

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JTable table;
    private String username;

    public ViewFlightBookings(String username) {
        this.username = username;

        setTitle("Safar - My Flight Bookings");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JLabel heading = new JLabel("✈️ My Flight Bookings", SwingConstants.CENTER);
        heading.setFont(new Font("Poppins", Font.BOLD, 24));
        heading.setForeground(BRAND_BLUE);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Flight", "Airline", "From", "To", "Class", "Passengers", "Fare (₹)", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(FONT_PLAIN_14);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(BRAND_TEAL);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(FONT_BOLD_16);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // --- Load Data from DB ---
        loadFlightBookings();

        // --- Back Button ---
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setBackground(BRAND_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(FONT_BOLD_16);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            this.dispose();
            new Dashboard(username).setVisible(true);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(CONTENT_BG);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // --- Fetch Bookings from DB ---
    private void loadFlightBookings() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String query = """
                SELECT fb.flight_code, f.flight_name, f.airline, f.from_city, f.to_city, 
                       fb.class_type, fb.passengers, fb.fare, fb.status
                FROM flight_bookings fb
                JOIN flights f ON fb.flight_code = f.flight_code
                WHERE fb.username = ?
                ORDER BY fb.id DESC
            """;

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                Object[] row = {
                    rs.getString("flight_name") + " (" + rs.getString("flight_code") + ")",
                    rs.getString("airline"),
                    rs.getString("from_city"),
                    rs.getString("to_city"),
                    rs.getString("class_type"),
                    rs.getInt("passengers"),
                    rs.getDouble("fare"),
                    rs.getString("status")
                };
                model.addRow(row);
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No flight bookings found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading flight bookings from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ViewFlightBookings("Abhinav");
    }
}
