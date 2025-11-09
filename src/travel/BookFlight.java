package travel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class BookFlight extends JFrame implements ActionListener {

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Font FONT_BOLD_20 = new Font("Poppins", Font.BOLD, 20);
    private static final Font FONT_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JComboBox<String> fromBox, toBox, classBox, airlineBox, passengerBox;
    private JTextField dateField;
    private JButton searchButton;
    private JTable flightTable;

    public BookFlight() {
        setTitle("Safar - Book Flight Ticket");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BRAND_BLUE);
        header.setPreferredSize(new Dimension(1000, 80));
        JLabel title = new JLabel("✈️ Book Your Flight Ticket", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Search Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        formPanel.setLayout(new GridLayout(2, 4, 15, 15));

        fromBox = new JComboBox<>(new String[]{"Delhi", "Mumbai", "Chennai", "Kolkata", "Goa", "Bangalore"});
        toBox = new JComboBox<>(new String[]{"Goa", "Delhi", "Mumbai", "Chennai", "Hyderabad", "Jaipur"});
        classBox = new JComboBox<>(new String[]{"Economy", "Premium Economy", "Business", "First Class"});
        airlineBox = new JComboBox<>(new String[]{"Any", "IndiGo", "Air India", "Vistara", "SpiceJet", "Go First"});
        passengerBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});

        dateField = new JTextField("YYYY-MM-DD");
        dateField.setFont(FONT_PLAIN_14);
        dateField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        formPanel.add(labeledField("From", fromBox));
        formPanel.add(labeledField("To", toBox));
        formPanel.add(labeledField("Journey Date", dateField));
        formPanel.add(labeledField("Class", classBox));
        formPanel.add(labeledField("Airline", airlineBox));
        formPanel.add(labeledField("Passengers", passengerBox));

        searchButton = new JButton("Search Flights");
        searchButton.setBackground(BRAND_TEAL);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(FONT_BOLD_14);
        searchButton.addActionListener(this);
        searchButton.setFocusPainted(false);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(CARD_BG);
        btnPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Flight Table
        String[] columns = {"Flight", "Departure", "Arrival", "Duration", "Airline", "Fare (₹)", "Availability", "Action"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 7;
            }
        };
        flightTable = new JTable(model);
        flightTable.setRowHeight(35);
        flightTable.setFont(FONT_PLAIN_14);
        flightTable.getTableHeader().setFont(FONT_BOLD_14);
        flightTable.getTableHeader().setBackground(BRAND_TEAL);
        flightTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(flightTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel labeledField(String text, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(CARD_BG);
        JLabel l = new JLabel(text);
        l.setFont(FONT_BOLD_14);
        l.setForeground(TEXT_DARK);
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String from = (String) fromBox.getSelectedItem();
            String to = (String) toBox.getSelectedItem();
            String date = dateField.getText().trim();
            String cls = (String) classBox.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Departure and Destination cannot be same!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Enter valid date (YYYY-MM-DD)", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            loadFlights(from, to, cls);
        }
    }

    private void loadFlights(String from, String to, String cls) {
        DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
        model.setRowCount(0);

        Object[][] flights = {
            {"AI 202", "06:30", "09:45", "3h 15m", "Air India", 7200, "Available", "Book Now"},
            {"6E 152", "08:00", "10:45", "2h 45m", "IndiGo", 4800, "Available", "Book Now"},
            {"SG 303", "14:15", "17:30", "3h 15m", "SpiceJet", 5300, "WL 1", "Book Now"},
            {"UK 812", "19:00", "22:10", "3h 10m", "Vistara", 8900, "Available", "Book Now"}
        };
        for (Object[] row : flights) model.addRow(row);

        flightTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        flightTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    // Renderers for buttons in table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(BRAND_BLUE);
            setForeground(Color.WHITE);
            setFont(FONT_BOLD_14);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox cb, JFrame parent) {
            super(cb);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(BRAND_BLUE);
            button.setForeground(Color.WHITE);
            button.setFont(FONT_BOLD_14);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int col) {
            label = value == null ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                JOptionPane.showMessageDialog(null, "Flight booked successfully!", "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
            clicked = false;
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookFlight::new);
    }
}
