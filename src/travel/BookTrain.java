package travel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * BookTrain.java
 * -----------------------
 * Fetches train data from database, allows users to search and book trains.
 * On booking, deducts fare from wallet and saves the booking in train_bookings table.
 * (This version ignores journey_date in the SQL filter)
 */
public class BookTrain extends JFrame implements ActionListener {

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Font FONT_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    // --- Components ---
    private JComboBox<String> fromBox, toBox, classBox, passengerBox;
    private JTextField dateField;
    private JTable trainTable;
    private JButton searchButton, backButton;
    private String username;

    public BookTrain(String username) {
        this.username = username;

        setTitle("Safar - Book Train Ticket");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(20, 20));

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BRAND_BLUE);
        JLabel title = new JLabel("🚆 Book Your Train Ticket", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- Search Panel ---
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fromBox = new JComboBox<>(new String[]{
            "Delhi", "Mumbai", "Kolkata", "Chennai", "Goa", "Jaipur",
            "Hyderabad", "Bengaluru", "Ahmedabad", "Pune", "Lucknow"
        });
        toBox = new JComboBox<>(new String[]{
            "Delhi", "Mumbai", "Kolkata", "Chennai", "Goa", "Jaipur",
            "Hyderabad", "Bengaluru", "Ahmedabad", "Pune", "Lucknow"
        });
        classBox = new JComboBox<>(new String[]{
            "Sleeper", "3A", "2A", "1A", "CC"
        });
        passengerBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});
        dateField = new JTextField("YYYY-MM-DD");

        formPanel.add(labeledField("From", fromBox));
        formPanel.add(labeledField("To", toBox));
        formPanel.add(labeledField("Journey Date", dateField));
        formPanel.add(labeledField("Class", classBox));
        formPanel.add(labeledField("Passengers", passengerBox));

        // --- Buttons ---
        searchButton = createButton("Search Trains", BRAND_TEAL);
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

        // --- Table Setup ---
        String[] cols = {"Train", "Departure", "Arrival", "Duration", "Class", "Fare (₹)", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c == 7; }
        };
        trainTable = new JTable(model);
        trainTable.setRowHeight(35);
        trainTable.getTableHeader().setFont(FONT_BOLD_14);
        trainTable.getTableHeader().setBackground(BRAND_TEAL);
        trainTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(trainTable);
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

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Source and destination cannot be the same!", "Invalid Route", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadTrains(from, to, cls);
        }
    }

    // --- Load Trains from Database (no journey_date filter) ---
    private void loadTrains(String from, String to, String cls) {
        DefaultTableModel model = (DefaultTableModel) trainTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM trains WHERE from_station=? AND to_station=? AND class_type=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, from);
            stmt.setString(2, to);
            stmt.setString(3, cls);

            ResultSet rs = stmt.executeQuery();
            boolean found = false;

            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getString("train_name"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getString("duration"),
                    rs.getString("class_type"),
                    rs.getDouble("fare"),
                    "Available",
                    "Book Now"
                };
                model.addRow(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No trains available for this route.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                trainTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
                trainTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading trains from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Renderer for "Book Now" Buttons ---
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
        private String trainName;
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
            trainName = (String) t.getValueAt(r, 0);
            btn.setText("Book Now");
            clicked = true;
            return btn;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                bookTrain(trainName);
            }
            clicked = false;
            return "Book Now";
        }
    }

    // --- Booking Logic ---
    private void bookTrain(String trainName) {
        try (Connection conn = DBConnection.getConnection()) {
            // Get selected fare
            PreparedStatement ps1 = conn.prepareStatement("SELECT fare FROM trains WHERE train_name=?");
            ps1.setString(1, trainName);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) return;
            double fare = rs.getDouble("fare");
            int passengers = Integer.parseInt((String) passengerBox.getSelectedItem());
            double totalFare = fare * passengers;

            // Check wallet balance
            PreparedStatement psBalance = conn.prepareStatement("SELECT wallet_balance FROM users WHERE username=?");
            psBalance.setString(1, username);
            ResultSet rsBal = psBalance.executeQuery();
            if (!rsBal.next()) return;
            double wallet = rsBal.getDouble("wallet_balance");

            if (wallet < totalFare) {
                JOptionPane.showMessageDialog(this, "Insufficient wallet balance! Please add money.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Deduct balance
            PreparedStatement ps2 = conn.prepareStatement("UPDATE users SET wallet_balance = wallet_balance - ? WHERE username=?");
            ps2.setDouble(1, totalFare);
            ps2.setString(2, username);
            ps2.executeUpdate();

            // Save booking
            PreparedStatement ps3 = conn.prepareStatement(
                "INSERT INTO train_bookings (username, train_name, class_type, passengers, fare, status) VALUES (?, ?, ?, ?, ?, 'Confirmed')");
            ps3.setString(1, username);
            ps3.setString(2, trainName);
            ps3.setString(3, (String) classBox.getSelectedItem());
            ps3.setInt(4, passengers);
            ps3.setDouble(5, totalFare);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "✅ Booking successful!\n" +
                    "Train: " + trainName + "\n" +
                    "Passengers: " + passengers + "\n" +
                    "Total Fare: ₹" + totalFare,
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while booking train.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BookTrain("Abhinav");
    }
}
