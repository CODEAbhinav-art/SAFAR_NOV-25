package travel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Safar Admin Dashboard
 * -----------------------
 * Displays an admin control panel integrated with SQL database.
 * Features:
 *  - View system overview (user count, bookings, revenue)
 *  - Manage users (list all, delete)
 *  - View train and flight bookings
 */
public class AdminDashboard extends JFrame implements ActionListener {

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color ACCENT_RED = new Color(220, 53, 69);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JPanel dynamicContent;
    private String adminUsername;
    private JTable userTable;
    private DefaultTableModel userModel;

    public AdminDashboard(String username) {
        this.adminUsername = username == null ? "Admin" : username;
        setTitle("Safar - Admin Dashboard");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        // ---------- LEFT SIDEBAR ----------
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(BRAND_BLUE);
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Admin Control</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        sidebar.add(logo, BorderLayout.NORTH);

        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.add(Box.createVerticalStrut(40));

        String[] tabs = {
            "Dashboard Overview",
            "Manage Users",
            "Train Bookings",
            "Flight Bookings",
            "Packages"
        };

        for (String tab : tabs) {
            JButton btn = createNavButton(tab);
            btn.addActionListener(this);
            navPanel.add(btn);
            navPanel.add(Box.createVerticalStrut(10));
        }

        JButton logout = brandButton("Logout", ACCENT_RED);
        logout.addActionListener(e -> {
            dispose();
            new AdminLogin().setVisible(true);
        });

        navPanel.add(Box.createVerticalGlue());
        sidebar.add(navPanel, BorderLayout.CENTER);
        sidebar.add(logout, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // ---------- MAIN CONTENT ----------
        dynamicContent = new JPanel(new BorderLayout());
        dynamicContent.setBackground(CONTENT_BG);
        add(dynamicContent, BorderLayout.CENTER);

        loadDashboardOverview();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        dynamicContent.removeAll();

        switch (cmd) {
            case "Dashboard Overview" -> loadDashboardOverview();
            case "Manage Users" -> loadUserManager();
            case "Train Bookings" -> loadTrainBookings();
            case "Flight Bookings" -> loadFlightBookings();
            case "Packages" -> loadPackageManager();
        }

        dynamicContent.revalidate();
        dynamicContent.repaint();
    }

    // ---------- DASHBOARD OVERVIEW ----------
    private void loadDashboardOverview() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(CONTENT_BG);

        try (Connection conn = DBConnection.getConnection()) {
            int users = count(conn, "SELECT COUNT(*) FROM users");
            int trainBookings = count(conn, "SELECT COUNT(*) FROM train_bookings");
            int flightBookings = count(conn, "SELECT COUNT(*) FROM flight_bookings");
            double revenue = sum(conn, "SELECT SUM(fare) FROM train_bookings") + sum(conn, "SELECT SUM(fare) FROM flight_bookings");

            panel.add(makeInfoCard("Total Users", String.valueOf(users), BRAND_BLUE));
            panel.add(makeInfoCard("Train Bookings", String.valueOf(trainBookings), BRAND_TEAL));
            panel.add(makeInfoCard("Flight Bookings", String.valueOf(flightBookings), new Color(0xFF9800)));
            panel.add(makeInfoCard("Revenue", "₹" + String.format("%.2f", revenue), new Color(0x4CAF50)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        dynamicContent.add(panel, BorderLayout.CENTER);
    }

    private int count(Connection conn, String query) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private double sum(Connection conn, String query) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getDouble(1);
    }

    // ---------- MANAGE USERS ----------
    private void loadUserManager() {
        String[] cols = {"Username", "Full Name", "Email", "Wallet (₹)", "Delete"};
        userModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c == 4; }
        };

        // Fetch users from database
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT username, name, email, wallet_balance FROM users";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userModel.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDouble("wallet_balance"),
                    "Delete"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        userTable = new JTable(userModel);
        userTable.setFont(FONT_PLAIN_14);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(FONT_BOLD_16);
        userTable.getTableHeader().setBackground(BRAND_TEAL);
        userTable.getTableHeader().setForeground(Color.WHITE);

        // Add delete button column
        userTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Delete").setCellEditor(new DeleteUserEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(userTable);
        dynamicContent.add(scroll, BorderLayout.CENTER);
    }

    // Button renderer for "Delete" column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(ACCENT_RED);
            setForeground(Color.WHITE);
            setFont(FONT_PLAIN_14);
        }

        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            setText(v == null ? "" : v.toString());
            return this;
        }
    }

    // Button editor for Delete action
    class DeleteUserEditor extends DefaultCellEditor {
        private JButton btn;
        private String username;
        private boolean clicked;

        public DeleteUserEditor(JCheckBox cb) {
            super(cb);
            btn = new JButton("Delete");
            btn.setOpaque(true);
            btn.setBackground(ACCENT_RED);
            btn.setForeground(Color.WHITE);
            btn.setFont(FONT_PLAIN_14);
            btn.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            username = (String) t.getValueAt(r, 0);
            clicked = true;
            return btn;
        }

        public Object getCellEditorValue() {
            if (clicked) deleteUser(username);
            clicked = false;
            return "Delete";
        }
    }

    private void deleteUser(String username) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user '" + username + "' and their bookings?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                // Delete associated bookings first
                PreparedStatement ps1 = conn.prepareStatement("DELETE FROM train_bookings WHERE username=?");
                ps1.setString(1, username);
                ps1.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement("DELETE FROM flight_bookings WHERE username=?");
                ps2.setString(1, username);
                ps2.executeUpdate();

                // Delete the user
                PreparedStatement ps3 = conn.prepareStatement("DELETE FROM users WHERE username=?");
                ps3.setString(1, username);
                ps3.executeUpdate();

                JOptionPane.showMessageDialog(this, "User '" + username + "' deleted successfully.");
                loadUserManager(); // Refresh
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ---------- TRAIN BOOKINGS ----------
    private void loadTrainBookings() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Username", "Train", "Date", "Class", "Passengers", "Fare (₹)", "Status"}, 0);

        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.prepareStatement("SELECT * FROM train_bookings").executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("train_name"),
                        rs.getDate("journey_date"),
                        rs.getString("class_type"),
                        rs.getInt("passengers"),
                        rs.getDouble("fare"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dynamicContent.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
    }

    // ---------- FLIGHT BOOKINGS ----------
    private void loadFlightBookings() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Username", "Flight Code", "Class", "Passengers", "Fare (₹)", "Status"}, 0);

        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.prepareStatement("SELECT * FROM flight_bookings").executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("flight_code"),
                        rs.getString("class_type"),
                        rs.getInt("passengers"),
                        rs.getDouble("fare"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dynamicContent.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
    }

    // ---------- PACKAGES ----------
    private void loadPackageManager() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Package Name", "Price (₹)", "Bookings"}, 0);

        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.prepareStatement("SELECT * FROM packages").executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("bookings")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dynamicContent.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
    }

    // ---------- UI HELPERS ----------
    private JPanel makeInfoCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        JLabel l1 = new JLabel(title, SwingConstants.CENTER);
        l1.setForeground(TEXT_LIGHT);
        l1.setFont(FONT_PLAIN_14);
        JLabel l2 = new JLabel(value, SwingConstants.CENTER);
        l2.setFont(FONT_BOLD_16);
        l2.setForeground(color);
        card.add(l1, BorderLayout.NORTH);
        card.add(l2, BorderLayout.CENTER);
        return card;
    }

    private JButton brandButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(FONT_PLAIN_14);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND_BLUE);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_PLAIN_14);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(0x3366CC)); }
            public void mouseExited(MouseEvent e) { b.setBackground(BRAND_BLUE); }
        });
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard("admin"));
    }
}
