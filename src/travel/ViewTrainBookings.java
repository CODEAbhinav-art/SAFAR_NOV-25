package travel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * ViewTrainBookings.java
 * -------------------------
 * Displays all train bookings made by the currently logged-in user.
 * Fetches data dynamically from the 'train_bookings' table in MySQL.
 */
public class ViewTrainBookings extends JFrame {

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private String username;
    private JTable bookingsTable;

    public ViewTrainBookings(String username) {
        this.username = username;

        setTitle("Safar - My Train Bookings");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(10, 10));

        // ---------- HEADER ----------
        JLabel heading = new JLabel("🚆 My Train Bookings", SwingConstants.CENTER);
        heading.setFont(new Font("Poppins", Font.BOLD, 24));
        heading.setForeground(BRAND_BLUE);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // ---------- TABLE SETUP ----------
        String[] columns = {"Train Name", "Journey Date", "Class", "Passengers", "Fare (₹)", "Status", "Booked On"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        bookingsTable = new JTable(model);
        bookingsTable.setFont(FONT_PLAIN_14);
        bookingsTable.setRowHeight(30);
        bookingsTable.getTableHeader().setBackground(BRAND_TEAL);
        bookingsTable.getTableHeader().setForeground(Color.WHITE);
        bookingsTable.getTableHeader().setFont(FONT_BOLD_16);

        JScrollPane scroll = new JScrollPane(bookingsTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // ---------- FOOTER / BACK BUTTON ----------
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

        // ---------- LOAD USER BOOKINGS ----------
        loadUserBookings(username);

        setVisible(true);
    }

    /**
     * Fetches all bookings for the logged-in user from the database.
     */
    private void loadUserBookings(String username) {
        DefaultTableModel model = (DefaultTableModel) bookingsTable.getModel();
        model.setRowCount(0); // clear previous rows

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM train_bookings WHERE username = ? ORDER BY booked_on DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getString("train_name"),
                    rs.getDate("journey_date"),
                    rs.getString("class_type"),
                    rs.getInt("passengers"),
                    rs.getDouble("fare"),
                    rs.getString("status"),
                    rs.getTimestamp("booked_on")
                };
                model.addRow(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No bookings found for your account.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching booking data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewTrainBookings("Abhinav"));
    }
}
