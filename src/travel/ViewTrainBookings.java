package travel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ViewTrainBookings extends JFrame {

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    public ViewTrainBookings() {
        setTitle("Safar - My Train Bookings");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel heading = new JLabel("🚆 My Train Bookings", SwingConstants.CENTER);
        heading.setFont(new Font("Poppins", Font.BOLD, 24));
        heading.setForeground(BRAND_BLUE);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Train", "Date", "From", "To", "Class", "Fare (₹)", "Status"};
        Object[][] data = {
            {"Rajdhani Express", "2024-02-12", "Delhi", "Mumbai", "2A", 1420, "Confirmed"},
            {"Duronto Express", "2024-03-05", "Goa", "Bangalore", "3A", 1150, "Waitlisted"},
            {"Garib Rath", "2024-04-18", "Jaipur", "Delhi", "Sleeper", 800, "Cancelled"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(FONT_PLAIN_14);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(BRAND_TEAL);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(FONT_BOLD_16);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // --- Back Button ---
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setBackground(BRAND_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(FONT_BOLD_16);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            this.dispose();  // close this window
            new Dashboard("User").setVisible(true);  // reopen dashboard
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(CONTENT_BG);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ViewTrainBookings();
    }
}
