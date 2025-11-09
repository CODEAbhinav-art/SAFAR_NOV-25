package travel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color ACCENT_ORANGE = new Color(255, 165, 64);
    private static final Color ACCENT_RED = new Color(220, 53, 69);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);

    private static final Font FONT_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JPanel dynamicContent;
    private DefaultTableModel userModel, packageModel;
    private String username;

    public AdminDashboard(String username) {
        this.username = username == null ? "Admin" : username;
        setTitle("Safar - Admin Dashboard");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        // ---------- LEFT NAVIGATION ----------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(280, 0));
        leftPanel.setBackground(BRAND_BLUE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Admin Control</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.NORTH);

        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.add(Box.createVerticalStrut(40));

        String[] buttons = {
            "Dashboard Overview",
            "Manage Users",
            "Manage Train Bookings",
            "Manage Flight Bookings",
            "Manage Packages",
            "View Analytics"
        };

        for (String label : buttons) {
            JButton btn = createNavButton(label);
            btn.addActionListener(this);
            navPanel.add(btn);
            navPanel.add(Box.createVerticalStrut(10));
        }
        navPanel.add(Box.createVerticalGlue());
        leftPanel.add(navPanel, BorderLayout.CENTER);

        JButton logout = brandButton("Logout", ACCENT_RED, Color.WHITE);
        logout.addActionListener(e -> {
            dispose();
            new AdminLogin().setVisible(true);
        });
        leftPanel.add(logout, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // ---------- MAIN CONTENT ----------
        JPanel contentPanel = new JPanel(new BorderLayout(18, 18));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(contentPanel, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Welcome, " + this.username);
        title.setFont(FONT_BOLD_22);
        title.setForeground(TEXT_DARK);
        header.add(title, BorderLayout.WEST);
        JButton refresh = brandButton("Refresh", BRAND_TEAL, Color.WHITE);
        header.add(refresh, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        // Dynamic center content
        dynamicContent = new JPanel(new BorderLayout());
        dynamicContent.setBackground(CONTENT_BG);
        contentPanel.add(dynamicContent, BorderLayout.CENTER);

        loadDashboardOverview();
        setVisible(true);
    }

    // ---------- Navigation ----------
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        dynamicContent.removeAll();

        switch (cmd) {
            case "Dashboard Overview" -> loadDashboardOverview();
            case "Manage Users" -> loadUserManager();
            case "Manage Packages" -> loadPackageManager();
            case "Manage Train Bookings" -> loadTrainBookings();
            case "Manage Flight Bookings" -> loadFlightBookings();
            case "View Analytics" -> loadAnalyticsPanel();
        }

        dynamicContent.revalidate();
        dynamicContent.repaint();
    }

    // ---------- Sections ----------

    private void loadDashboardOverview() {
        JPanel overview = new JPanel(new BorderLayout(16, 16));
        overview.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        cards.add(makeInfoCard("Active Users", "1,245", BRAND_BLUE));
        cards.add(makeInfoCard("Total Bookings", "742", BRAND_TEAL));
        cards.add(makeInfoCard("Revenue", "₹8.3L", ACCENT_ORANGE));
        overview.add(cards, BorderLayout.NORTH);

        JPanel recent = new JPanel(new BorderLayout());
        recent.setBackground(CARD_BG);
        recent.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        JLabel title = new JLabel("Recent Activity");
        title.setFont(FONT_BOLD_16);
        title.setForeground(TEXT_DARK);
        recent.add(title, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("User 'Neha' booked AI 202");
        model.addElement("User 'Rahul' cancelled Rajdhani Express");
        model.addElement("Admin added new Package 'Himalayan Trek'");
        JList<String> list = new JList<>(model);
        list.setBackground(CARD_BG);
        list.setFont(FONT_PLAIN_14);
        recent.add(new JScrollPane(list), BorderLayout.CENTER);
        overview.add(recent, BorderLayout.CENTER);

        dynamicContent.add(overview, BorderLayout.CENTER);
    }

    private void loadUserManager() {
        String[] cols = {"User ID", "Name", "Email", "Bookings"};
        Object[][] data = {
            {"U101", "Ajay Sharma", "ajay@gmail.com", 3},
            {"U102", "Neha Patel", "neha@outlook.com", 5},
            {"U103", "Rahul Verma", "rahul@yahoo.com", 2}
        };

        userModel = new DefaultTableModel(data, cols);
        JTable table = createTable(userModel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton removeBtn = brandButton("Remove Selected User", ACCENT_RED, Color.WHITE);
        removeBtn.addActionListener(e -> removeSelectedRow(table, userModel, "user"));
        panel.add(removeBtn, BorderLayout.SOUTH);

        dynamicContent.add(panel, BorderLayout.CENTER);
    }

    private void loadPackageManager() {
        String[] cols = {"Package ID", "Name", "Price (₹)", "Bookings"};
        Object[][] data = {
            {"P01", "Himalayan Trek", 12500, 27},
            {"P02", "Beach Getaway Goa", 9800, 42}
        };

        packageModel = new DefaultTableModel(data, cols);
        JTable table = createTable(packageModel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(CONTENT_BG);

        JButton addBtn = brandButton("Add Package", BRAND_TEAL, Color.WHITE);
        JButton delBtn = brandButton("Delete Selected", ACCENT_RED, Color.WHITE);
        addBtn.addActionListener(e -> openAddPackageDialog());
        delBtn.addActionListener(e -> removeSelectedRow(table, packageModel, "package"));

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dynamicContent.add(panel, BorderLayout.CENTER);
    }

    private void loadTrainBookings() {
        String[] cols = {"Booking ID", "User", "Train", "Date", "Status"};
        Object[][] data = {
            {"T301", "Ajay Sharma", "Rajdhani Express", "2024-02-12", "Confirmed"},
            {"T302", "Rahul Verma", "Duronto Express", "2024-03-05", "Pending"}
        };
        dynamicContent.add(createTablePanel("Train Bookings", cols, data), BorderLayout.CENTER);
    }

    private void loadFlightBookings() {
        String[] cols = {"Booking ID", "User", "Flight", "Date", "Status"};
        Object[][] data = {
            {"F101", "Neha Patel", "AI 202", "2024-02-15", "Confirmed"},
            {"F102", "Rahul Verma", "UK 812", "2024-04-01", "Cancelled"}
        };
        dynamicContent.add(createTablePanel("Flight Bookings", cols, data), BorderLayout.CENTER);
    }

    // ✅ Reverted back to text summary analytics
    private void loadAnalyticsPanel() {
        JPanel analytics = new JPanel();
        analytics.setLayout(new BoxLayout(analytics, BoxLayout.Y_AXIS));
        analytics.setBackground(CONTENT_BG);
        analytics.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("📊 System Analytics", SwingConstants.LEFT);
        heading.setFont(FONT_BOLD_16);
        heading.setForeground(TEXT_DARK);
        analytics.add(heading);
        analytics.add(Box.createVerticalStrut(20));

        JLabel revenue = new JLabel("Total Revenue: ₹8.3 Lakh");
        JLabel bookings = new JLabel("Total Bookings: 742");
        JLabel users = new JLabel("Active Users: 1,245");
        JLabel growth = new JLabel("Platform Growth: 14.6% (QoQ)");
        JLabel topPackage = new JLabel("Most Booked Package: Beach Getaway Goa");

        for (JLabel lbl : new JLabel[]{revenue, bookings, users, growth, topPackage}) {
            lbl.setFont(FONT_PLAIN_14);
            lbl.setForeground(TEXT_LIGHT);
            analytics.add(lbl);
            analytics.add(Box.createVerticalStrut(8));
        }

        analytics.add(Box.createVerticalGlue());
        dynamicContent.add(analytics, BorderLayout.CENTER);
    }

    // ---------- Helper Methods ----------
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(FONT_PLAIN_14);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(BRAND_TEAL);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(FONT_BOLD_16);
        return table;
    }

    private JPanel createTablePanel(String title, String[] columns, Object[][] data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        JLabel heading = new JLabel(title, SwingConstants.LEFT);
        heading.setFont(FONT_BOLD_16);
        heading.setForeground(TEXT_DARK);
        panel.add(heading, BorderLayout.NORTH);
        JTable table = createTable(new DefaultTableModel(data, columns));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void removeSelectedRow(JTable table, DefaultTableModel model, String entity) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Remove selected " + entity + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, entity + " removed successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a " + entity + " first.");
        }
    }

    private void openAddPackageDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] fields = {
            "Package Name:", nameField,
            "Price (₹):", priceField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Package", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int price = Integer.parseInt(priceField.getText().trim());
                packageModel.addRow(new Object[]{"P" + (packageModel.getRowCount() + 1), name, price, 0});
                JOptionPane.showMessageDialog(this, "Package added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid price!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel makeInfoCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(FONT_PLAIN_14);
        t.setForeground(TEXT_LIGHT);
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(FONT_BOLD_16);
        v.setForeground(color);
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private JButton brandButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(FONT_PLAIN_14);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return b;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_PLAIN_14);
        btn.setBackground(BRAND_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0x3366CC)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(BRAND_BLUE); }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard("admin"));
    }
}
