package travel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * BookFlight.java
 * ----------------------------------
 * Allows users to search and book flights using data from MySQL.
 * Deducts fare from wallet and records booking in flight_bookings table.
 * (No journey_date filter — behaves like BookTrain)
 */
public class BookFlight extends JFrame implements ActionListener {

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Font FONT_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JComboBox<String> fromBox, toBox, classBox, airlineBox, passengerBox;
    private JTextField dateField;
    private JTable flightTable;
    private JButton searchButton, backButton;
    private String username;

    public BookFlight(String username) {
        this.username = username;

        setTitle("Safar - Book Flight Ticket");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(20, 20));

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BRAND_BLUE);
        JLabel title = new JLabel("✈️ Book Your Flight Ticket", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- Search Panel ---
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fromBox = new JComboBox<>(new String[]{
            "Delhi", "Mumbai", "Kolkata", "Chennai", "Goa", "Hyderabad", "Bengaluru", "Pune", "Ahmedabad", "Jaipur"
        });
        toBox = new JComboBox<>(new String[]{
            "Delhi", "Mumbai", "Kolkata", "Chennai", "Goa", "Hyderabad", "Bengaluru", "Pune", "Ahmedabad", "Jaipur"
        });
        classBox = new JComboBox<>(new String[]{"Economy", "Premium Economy", "Business", "First Class"});
        airlineBox = new JComboBox<>(new String[]{"Any", "IndiGo", "Air India", "Vistara", "SpiceJet", "Go First"});
        passengerBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});
        dateField = new JTextField("YYYY-MM-DD");

        formPanel.add(labeledField("From", fromBox));
        formPanel.add(labeledField("To", toBox));
        formPanel.add(labeledField("Journey Date", dateField));
        formPanel.add(labeledField("Class", classBox));
        formPanel.add(labeledField("Airline", airlineBox));
        formPanel.add(labeledField("Passengers", passengerBox));

        // --- Buttons ---
        searchButton = createButton("Search Flights", BRAND_TEAL);
        searchButton.addActionListener(this);

        backButton = createButton("← Back", BRAND_BLUE);
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard(username).setVisible(true);
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(CARD_BG);
        btnPanel.add(searchButton);
        btnPanel.add(backButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(formPanel, BorderLayout.CENTER);
        top.add(btnPanel, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // --- Table ---
        String[] cols = {"Flight", "Departure", "Arrival", "Duration", "Airline", "Class", "Fare (₹)", "Action"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c == 7; }
        };
        flightTable = new JTable(model);
        flightTable.setRowHeight(35);
        flightTable.getTableHeader().setFont(FONT_BOLD_14);
        flightTable.getTableHeader().setBackground(BRAND_TEAL);
        flightTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(flightTable);
        add(sp, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel labeledField(String text, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        JLabel l = new JLabel(text);
        l.setFont(FONT_BOLD_14);
        l.setForeground(TEXT_DARK);
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD_14);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    // --- Search Action ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String from = (String) fromBox.getSelectedItem();
            String to = (String) toBox.getSelectedItem();
            String cls = (String) classBox.getSelectedItem();
            String airline = (String) airlineBox.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Source and destination cannot be the same!", "Invalid Route", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadFlights(from, to, cls, airline);
        }
    }

    // --- Load Flights from Database (No date filter) ---
    private void loadFlights(String from, String to, String cls, String airline) {
        DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM flights WHERE from_city=? AND to_city=? AND class_type=?";
            if (!airline.equals("Any")) query += " AND airline=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, from);
            stmt.setString(2, to);
            stmt.setString(3, cls);
            if (!airline.equals("Any")) stmt.setString(4, airline);

            ResultSet rs = stmt.executeQuery();
            boolean found = false;

            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getString("flight_name") + " (" + rs.getString("flight_code") + ")",
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getString("duration"),
                    rs.getString("airline"),
                    rs.getString("class_type"),
                    rs.getDouble("fare"),
                    "Book Now"
                };
                model.addRow(row);
            }

            if (!found)
                JOptionPane.showMessageDialog(this, "No flights found for this route.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            else {
                flightTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
                flightTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading flights from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Renderer for Book Button ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(BRAND_BLUE);
            setForeground(Color.WHITE);
            setFont(FONT_BOLD_14);
        }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            setText(v == null ? "" : v.toString());
            return this;
        }
    }

    // --- Editor for Book Button ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton btn;
        private String flightName;
        private boolean clicked;

        public ButtonEditor(JCheckBox cb) {
            super(cb);
            btn = new JButton();
            btn.setOpaque(true);
            btn.setBackground(BRAND_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(FONT_BOLD_14);
            btn.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            flightName = (String) t.getValueAt(r, 0);
            btn.setText("Book Now");
            clicked = true;
            return btn;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                bookFlight(flightName);
            }
            clicked = false;
            return "Book Now";
        }
    }

    // --- Booking Logic ---
    private void bookFlight(String flightName) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("SELECT fare FROM flights WHERE CONCAT(flight_name, ' (', flight_code, ')')=?");
            ps1.setString(1, flightName);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) return;
            double fare = rs.getDouble("fare");
            int passengers = Integer.parseInt((String) passengerBox.getSelectedItem());
            double totalFare = fare * passengers;

            PreparedStatement psBalance = conn.prepareStatement("SELECT wallet_balance FROM users WHERE username=?");
            psBalance.setString(1, username);
            ResultSet rsBal = psBalance.executeQuery();
            if (!rsBal.next()) return;
            double wallet = rsBal.getDouble("wallet_balance");

            if (wallet < totalFare) {
                JOptionPane.showMessageDialog(this, "Insufficient wallet balance! Please add money.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement("UPDATE users SET wallet_balance = wallet_balance - ? WHERE username=?");
            ps2.setDouble(1, totalFare);
            ps2.setString(2, username);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement(
                "INSERT INTO flight_bookings (username, flight_code, class_type, passengers, fare, status) VALUES (?, ?, ?, ?, ?, 'Confirmed')");
            String code = flightName.substring(flightName.indexOf('(') + 1, flightName.indexOf(')'));
            ps3.setString(1, username);
            ps3.setString(2, code);
            ps3.setString(3, (String) classBox.getSelectedItem());
            ps3.setInt(4, passengers);
            ps3.setDouble(5, totalFare);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "✅ Flight booked successfully!\n" +
                    "Flight: " + flightName + "\n" +
                    "Passengers: " + passengers + "\n" +
                    "Total Fare: ₹" + totalFare,
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking flight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BookFlight("Abhinav");
    }
}
