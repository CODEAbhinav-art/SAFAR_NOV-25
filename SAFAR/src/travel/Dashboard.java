package travel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Dashboard.java
 * -----------------------------------
 * Displays the main Safar Dashboard after user login.
 * Shows wallet balance, travel journal, community, and navigation options.
 * 
 * All modules like BookTrain, ViewTrainBookings, etc. are integrated with SQL.
 */
public class Dashboard extends JFrame implements ActionListener {

    // --- THEME COLORS ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color ACCENT_ORANGE = new Color(255, 87, 34);
    private static final Color ACCENT_RED = new Color(220, 53, 69);

    // --- FONTS ---
    private static final Font FONT_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);
    private static final Font FONT_PLAIN_12 = new Font("Poppins", Font.PLAIN, 12);

    private final String username;
    private JLabel walletLabel;

    public Dashboard(String username) {
        this.username = username != null ? username : "Traveler";

        setTitle("Safar - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 840);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        // ==============================
        // LEFT NAVIGATION PANEL
        // ==============================
        JPanel leftNav = new JPanel(new BorderLayout());
        leftNav.setPreferredSize(new Dimension(300, 0));
        leftNav.setBackground(BRAND_BLUE);
        leftNav.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Your Journey Awaits</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 24));
        logo.setForeground(COLOR_WHITE);
        leftNav.add(logo, BorderLayout.NORTH);

        // Navigation Buttons
        JPanel navButtons = new JPanel();
        navButtons.setOpaque(false);
        navButtons.setLayout(new BoxLayout(navButtons, BoxLayout.Y_AXIS));
        navButtons.add(Box.createVerticalStrut(40));

        String[] options = {
                "Update Personal Details",
                "View Personal Details",
                "Book Flight Ticket",
                "View Flight Bookings",
                "Book Train Ticket",
                "View Train Bookings"
        };

        for (String opt : options) {
            JButton btn = createNavButton(opt);
            btn.addActionListener(this);
            navButtons.add(btn);
            navButtons.add(Box.createVerticalStrut(10));
        }

        navButtons.add(Box.createVerticalGlue());
        leftNav.add(navButtons, BorderLayout.CENTER);

        JButton logoutBtn = brandButton("Log Out", ACCENT_RED, COLOR_WHITE);
        logoutBtn.addActionListener(this);
        leftNav.add(logoutBtn, BorderLayout.SOUTH);

        add(leftNav, BorderLayout.WEST);

        // ==============================
        // MAIN CONTENT AREA
        // ==============================
        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setBackground(CONTENT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(content, BorderLayout.CENTER);

        // --- Header ---
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome, " + username + "!");
        welcome.setFont(FONT_BOLD_22);
        welcome.setForeground(TEXT_DARK);
        header.add(welcome);

        JLabel sub = new JLabel("Plan, Book, and Manage your perfect journey — all in one place.");
        sub.setFont(FONT_PLAIN_14);
        sub.setForeground(TEXT_LIGHT);
        header.add(sub);

        content.add(header, BorderLayout.NORTH);

        // --- Main Grid Layout ---
        JPanel grid = new JPanel(new GridLayout(1, 2, 18, 18));
        grid.setOpaque(false);
        content.add(grid, BorderLayout.CENTER);

        // LEFT COLUMN PANELS
        JPanel leftCol = new JPanel(new GridLayout(2, 1, 0, 18));
        leftCol.setOpaque(false);

        // RIGHT COLUMN PANELS
        JPanel rightCol = new JPanel(new GridLayout(2, 1, 0, 18));
        rightCol.setOpaque(false);

        grid.add(leftCol);
        grid.add(rightCol);

        // ==============================
        // WALLET SECTION
        // ==============================
        JPanel walletPanel = new JPanel();
        styleCardPanel(walletPanel, "My Safar Wallet");
        walletPanel.setLayout(new BoxLayout(walletPanel, BoxLayout.Y_AXIS));

        walletLabel = new JLabel();
        walletLabel.setFont(FONT_BOLD_16);
        walletLabel.setForeground(TEXT_DARK);
        walletPanel.add(walletLabel);
        walletPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        double walletBalance = fetchWalletBalance(username);
        walletLabel.setText("Current Balance: ₹" + String.format("%.2f", walletBalance));

        JLabel recent = new JLabel("Recent Transactions:");
        recent.setFont(FONT_BOLD_14);
        recent.setForeground(TEXT_DARK);
        walletPanel.add(recent);

        JLabel t1 = new JLabel("• Last Added: ₹" + String.format("%.2f", walletBalance) + " (Approx)");
        t1.setFont(FONT_PLAIN_12);
        t1.setForeground(TEXT_LIGHT);
        walletPanel.add(t1);
        walletPanel.add(Box.createVerticalGlue());

        JButton addMoney = brandButton("Add Money", BRAND_TEAL, COLOR_WHITE);
        addMoney.setAlignmentX(Component.LEFT_ALIGNMENT);
        addMoney.addActionListener(e -> new WalletTopUpDialog(this, username, walletLabel));
        walletPanel.add(addMoney);

        leftCol.add(walletPanel);

        // ==============================
        // JOURNAL PANEL
        // ==============================
        JPanel journalPanel = new JPanel();
        styleCardPanel(journalPanel, "Travel Journal");
        journalPanel.setLayout(new BoxLayout(journalPanel, BoxLayout.Y_AXIS));

        JTextArea journalArea = new JTextArea(10, 30);
        journalArea.setText("2024-06-12: Flew to Goa — stunning beaches!\n\n"
                + "2024-09-10: Train to Manali — loved the snow peaks!");
        journalArea.setEditable(false);
        journalArea.setWrapStyleWord(true);
        journalArea.setLineWrap(true);
        journalArea.setFont(FONT_PLAIN_12);
        journalArea.setBackground(CONTENT_BG);
        journalPanel.add(new JScrollPane(journalArea));

        leftCol.add(journalPanel);

        // ==============================
        // COMMUNITY PANEL
        // ==============================
        JPanel forumPanel = new JPanel(new BorderLayout(10, 10));
        styleCardPanel(forumPanel, "Travel Community Forum");

        JTextArea posts = new JTextArea();
        posts.setEditable(false);
        posts.setBackground(CONTENT_BG);
        posts.setFont(FONT_PLAIN_12);
        posts.setText("""
                • Meera (Delhi): Any tips for solo travel in Bali?
                • Rajesh: Check out Ubud — peaceful and scenic!
                • Anita: Best places in North Goa for couples?
                • Suraj: Try Agonda Beach — calm and clean.
                """);

        JScrollPane postsScroll = new JScrollPane(posts);
        forumPanel.add(postsScroll, BorderLayout.CENTER);

        JButton newPost = brandButton("New Post", BRAND_TEAL, COLOR_WHITE);
        forumPanel.add(newPost, BorderLayout.SOUTH);
        rightCol.add(forumPanel);

        // ==============================
        // UPCOMING TRIPS
        // ==============================
        JPanel tripsPanel = new JPanel(new BorderLayout());
        styleCardPanel(tripsPanel, "Upcoming Trips");

        String[] columns = {"Destination", "Dates", "Status"};
        Object[][] data = {
                {"Mumbai", "2025-02-12", "Confirmed"},
                {"Goa", "2025-03-01", "Pending"},
                {"Shimla", "2025-04-05", "Booked"}
        };

        JTable table = new JTable(data, columns);
        table.setBackground(CARD_BG);
        table.getTableHeader().setBackground(BRAND_TEAL);
        table.getTableHeader().setForeground(COLOR_WHITE);
        table.setRowHeight(25);
        tripsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        rightCol.add(tripsPanel);

        setVisible(true);
    }

    // ==============================
    // DATABASE: Fetch Wallet Balance
    // ==============================
    private double fetchWalletBalance(String username) {
        double balance = 0.0;
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT wallet_balance FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) balance = rs.getDouble("wallet_balance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    // ==============================
    // STYLING HELPERS
    // ==============================
    private void styleCardPanel(JPanel panel, String title) {
        panel.setBackground(CARD_BG);
        Border line = BorderFactory.createLineBorder(BORDER_COLOR);
        Border inner = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        TitledBorder titled = BorderFactory.createTitledBorder(line, title, TitledBorder.LEFT, TitledBorder.TOP, FONT_BOLD_16, TEXT_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(titled, inner));
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND_BLUE);
        b.setForeground(new Color(230, 240, 255));
        b.setFont(FONT_BOLD_14);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(0x3366CC)); }
            public void mouseExited(MouseEvent e) { b.setBackground(BRAND_BLUE); }
        });
        return b;
    }

    private JButton brandButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(FONT_BOLD_14);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ==============================
    // ACTION HANDLER
    // ==============================
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "Book Train Ticket":
                new BookTrain(username).setVisible(true);
                dispose();
                break;

            case "Book Flight Ticket":
                new BookFlight(username).setVisible(true);
                dispose();
                break;

            case "View Train Bookings":
                new ViewTrainBookings(username).setVisible(true);
                dispose();
                break;

            case "View Flight Bookings":
                new ViewFlightBookings(username).setVisible(true);
                dispose();
                break;

            case "Update Personal Details":
                new UpdateCustomer(username).setVisible(true);
                dispose();
                break;

            case "View Personal Details":
                new ViewCustomer(username).setVisible(true);
                dispose();
                break;

            case "Log Out":
                dispose();
                new Login().setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Action: " + cmd);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("Abhinav").setVisible(true));
    }
}
