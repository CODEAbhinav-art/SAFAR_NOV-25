package travel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ViewTrainBookings extends JFrame {

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

        JLabel heading = new JLabel("🚆 My Train Bookings", SwingConstants.CENTER);
        heading.setFont(FONT_BOLD_16);
        heading.setForeground(Color.BLACK);
        add(heading, BorderLayout.NORTH);

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
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ViewTrainBookings();
    }
}
