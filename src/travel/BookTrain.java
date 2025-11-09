package travel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BookTrain extends JFrame implements ActionListener {

    // Theme Colors
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);

    // Fonts
    private static final Font FONT_BOLD_20 = new Font("Poppins", Font.BOLD, 20);
    private static final Font FONT_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JComboBox<String> fromBox, toBox, classBox, passengerBox;
    private JTextField dateField;
    private JButton searchButton;
    private JTable trainTable;

    public BookTrain() {
        setTitle("Safar - Book Train Ticket");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout(20, 20));

        // --- Header Panel ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BRAND_BLUE);
        header.setPreferredSize(new Dimension(1000, 80));
        JLabel title = new JLabel("🚆 Book Your Train Ticket", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- Search Form Panel ---
        JPanel formPanel = new JPanel();
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        formPanel.setLayout(new GridLayout(2, 4, 15, 15));

        // Dropdowns & Inputs
        fromBox = new JComboBox<>(new String[]{"Delhi", "Mumbai", "Chennai", "Kolkata", "Goa", "Jaipur"});
        toBox = new JComboBox<>(new String[]{"Goa", "Delhi", "Mumbai", "Chennai", "Lucknow", "Hyderabad"});
        classBox = new JComboBox<>(new String[]{"Sleeper (SL)", "3A - AC 3 Tier", "2A - AC 2 Tier", "1A - AC First", "CC - Chair Car"});
        passengerBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});

        dateField = new JTextField("YYYY-MM-DD");
        dateField.setFont(FONT_PLAIN_14);
        dateField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        formPanel.add(labeledField("From Station", fromBox));
        formPanel.add(labeledField("To Station", toBox));
        formPanel.add(labeledField("Journey Date", dateField));
        formPanel.add(labeledField("Class", classBox));
        formPanel.add(labeledField("Passengers", passengerBox));

        searchButton = new JButton("Search Trains");
        searchButton.setBackground(BRAND_TEAL);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(FONT_BOLD_14);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(CARD_BG);
        btnPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CONTENT_BG);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // --- Train Results Table ---
        String[] columnNames = {"Train Name", "Departure", "Arrival", "Duration", "Class", "Fare (₹)", "Status", "Action"};
        Object[][] emptyData = {};

        DefaultTableModel model = new DefaultTableModel(emptyData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 7; // Only Book button column
            }
        };

        trainTable = new JTable(model);
        trainTable.setRowHeight(35);
        trainTable.setFont(FONT_PLAIN_14);
        trainTable.getTableHeader().setFont(FONT_BOLD_14);
        trainTable.getTableHeader().setBackground(BRAND_TEAL);
        trainTable.getTableHeader().setForeground(Color.WHITE);

        // Add scroll pane
        JScrollPane tableScroll = new JScrollPane(trainTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        add(tableScroll, BorderLayout.CENTER);

        // --- Footer Panel ---
        JPanel footer = new JPanel();
        footer.setBackground(CONTENT_BG);
        JLabel footText = new JLabel("Powered by SAFAR | © 2025 GSV Team");
        footText.setFont(FONT_PLAIN_14);
        footText.setForeground(TEXT_LIGHT);
        footer.add(footText);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel labeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(CARD_BG);
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BOLD_14);
        label.setForeground(TEXT_DARK);
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String from = (String) fromBox.getSelectedItem();
            String to = (String) toBox.getSelectedItem();
            String date = dateField.getText().trim();
            String cls = (String) classBox.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "Source and destination cannot be the same!", "Invalid Route", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (date.isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Enter journey date in YYYY-MM-DD format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadTrains(from, to, cls);
        }
    }

    private void loadTrains(String from, String to, String cls) {
        DefaultTableModel model = (DefaultTableModel) trainTable.getModel();
        model.setRowCount(0); // Clear old rows

        Object[][] trains = {
            {"Rajdhani Express", "06:00", "14:30", "8h 30m", cls, 1420, "Available", "Book Now"},
            {"Shatabdi Express", "09:15", "16:00", "6h 45m", cls, 1250, "Available", "Book Now"},
            {"Garib Rath", "11:00", "19:45", "8h 45m", cls, 990, "WL 2", "Book Now"},
            {"Duronto Express", "20:10", "06:00", "9h 50m", cls, 1600, "Available", "Book Now"}
        };

        for (Object[] row : trains) {
            model.addRow(row);
        }

        // Add custom cell renderer for "Book Now" button
        trainTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        trainTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    // --- Button Renderer ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(FONT_BOLD_14);
            setBackground(BRAND_BLUE);
            setForeground(Color.WHITE);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // --- Button Editor ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox, JFrame parent) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(BRAND_BLUE);
            button.setForeground(Color.WHITE);
            button.setFont(FONT_BOLD_14);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            label = value == null ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                JOptionPane.showMessageDialog(null, "Train booked successfully!", "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
            clicked = false;
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookTrain::new);
    }
}
