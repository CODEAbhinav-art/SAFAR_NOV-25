package travel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {

    // --- Theme Colors ---
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

    // --- Fonts ---
    private static final Font FONT_POPPINS_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_POPPINS_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_POPPINS_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_POPPINS_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);
    private static final Font FONT_POPPINS_PLAIN_12 = new Font("Poppins", Font.PLAIN, 12);

    private String username;

    Dashboard(String username) {
        this.username = username;
        setTitle("Safar - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 840);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        // ---------- LEFT NAVIGATION ----------
        JPanel leftNavPanel = new JPanel(new BorderLayout());
        leftNavPanel.setPreferredSize(new Dimension(300, 0));
        leftNavPanel.setBackground(BRAND_BLUE);
        leftNavPanel.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Your Journey Awaits</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 24));
        logo.setForeground(COLOR_WHITE);
        leftNavPanel.add(logo, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(Box.createVerticalStrut(40));

        String[] buttonLabels = {
            "Update Personal Details", "View Personal Details",
            "Book Flight Ticket", "View Flight Bookings",
            "Book Train Ticket", "View Train Bookings"
        };

        for (String label : buttonLabels) {
            JButton btn = createNavButton(label);
            btn.addActionListener(this);
            buttonsPanel.add(btn);
            buttonsPanel.add(Box.createVerticalStrut(10));
        }

        buttonsPanel.add(Box.createVerticalGlue());
        leftNavPanel.add(buttonsPanel, BorderLayout.CENTER);

        JButton logoutButton = brandButton("Log Out", ACCENT_RED, COLOR_WHITE);
        logoutButton.addActionListener(this);
        leftNavPanel.add(logoutButton, BorderLayout.SOUTH);
        add(leftNavPanel, BorderLayout.WEST);

        // ---------- RIGHT CONTENT ----------
        JPanel contentPanel = new JPanel(new BorderLayout(18, 18));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(contentPanel, BorderLayout.CENTER);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + (username.isEmpty() ? "Traveler!" : username + "!"));
        welcomeLabel.setFont(FONT_POPPINS_BOLD_22);
        welcomeLabel.setForeground(TEXT_DARK);
        headerPanel.add(welcomeLabel);

        JLabel sub = new JLabel("Plan, Book, and Manage your perfect journey — all in one place.");
        sub.setFont(FONT_POPPINS_PLAIN_14);
        sub.setForeground(TEXT_LIGHT);
        headerPanel.add(sub);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // ---------- MAIN CONTENT GRID ----------
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 18, 18));
        gridPanel.setOpaque(false);
        contentPanel.add(gridPanel, BorderLayout.CENTER);

        // LEFT COLUMN
        JPanel leftColumn = new JPanel(new GridLayout(2, 1, 0, 18));
        leftColumn.setOpaque(false);

        // RIGHT COLUMN
        JPanel rightColumn = new JPanel(new GridLayout(2, 1, 0, 18));
        rightColumn.setOpaque(false);

        gridPanel.add(leftColumn);
        gridPanel.add(rightColumn);

        // ---------- (1) WALLET FEATURE ----------
        JPanel walletPanel = new JPanel();
        styleCardPanel(walletPanel, "My Safar Wallet");
        walletPanel.setLayout(new BoxLayout(walletPanel, BoxLayout.Y_AXIS));

        JLabel balanceLabel = new JLabel("Current Balance: ₹5,200.00");
        balanceLabel.setFont(FONT_POPPINS_BOLD_16);
        balanceLabel.setForeground(TEXT_DARK);
        walletPanel.add(balanceLabel);
        walletPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel recentLabel = new JLabel("Recent Transactions:");
        recentLabel.setFont(FONT_POPPINS_BOLD_14);
        recentLabel.setForeground(TEXT_DARK);
        walletPanel.add(recentLabel);

        JLabel t1 = new JLabel("• Flight Booking - ₹2,500");
        JLabel t2 = new JLabel("• Added to Wallet - ₹3,000");
        JLabel t3 = new JLabel("• Train Ticket - ₹800");
        for (JLabel l : new JLabel[]{t1, t2, t3}) {
            l.setFont(FONT_POPPINS_PLAIN_12);
            l.setForeground(TEXT_LIGHT);
            walletPanel.add(l);
        }

        walletPanel.add(Box.createVerticalGlue());

        JButton addMoney = brandButton("Add Money", BRAND_TEAL, COLOR_WHITE);
        addMoney.setAlignmentX(Component.LEFT_ALIGNMENT);
        walletPanel.add(addMoney);
        leftColumn.add(walletPanel);

        // ---------- (2) TRAVEL JOURNAL (SCROLLABLE) ----------
        JPanel journalPanel = new JPanel();
        styleCardPanel(journalPanel, "Travel Journal");
        journalPanel.setLayout(new BoxLayout(journalPanel, BoxLayout.Y_AXIS));

        JTextArea journalArea = new JTextArea(10, 30);
        journalArea.setText("2023-09-21: Visited Jaipur City Palace – breathtaking architecture!\n\n"
                + "2023-10-03: Flew to Goa — beaches, music, and seafood galore!\n\n"
                + "2023-11-10: Took the train to Manali. The snow-capped peaks are surreal!\n\n"
                + "2023-12-01: Exploring Delhi's cultural blend — historic and modern.");
        journalArea.setWrapStyleWord(true);
        journalArea.setLineWrap(true);
        journalArea.setEditable(false);
        journalArea.setBackground(CONTENT_BG);
        journalArea.setForeground(TEXT_DARK);
        journalArea.setFont(FONT_POPPINS_PLAIN_12);

        JScrollPane journalScroll = new JScrollPane(journalArea);
        journalScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        journalPanel.add(journalScroll);

        JButton addEntry = brandButton("Add New Entry", ACCENT_ORANGE, COLOR_WHITE);
        addEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
        journalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        journalPanel.add(addEntry);
        leftColumn.add(journalPanel);

        // ---------- (3) TRAVEL COMMUNITY ----------
        JPanel forumPanel = new JPanel(new BorderLayout(10, 10));
        styleCardPanel(forumPanel, "Travel Community Forum");

        JTextArea posts = new JTextArea();
        posts.setEditable(false);
        posts.setBackground(CONTENT_BG);
        posts.setForeground(TEXT_DARK);
        posts.setFont(FONT_POPPINS_PLAIN_12);
        posts.setText("• Meera (Delhi): Any tips for solo travel in Bali?\n"
                + "• Rajesh: Check out Ubud — peaceful and scenic!\n"
                + "• Anita: Great places in North Goa for couples?\n"
                + "• Suraj: Try Agonda Beach — calm and clean.\n"
                + "---------------------------------------------\n");
        JScrollPane postsScroll = new JScrollPane(posts);
        postsScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        forumPanel.add(postsScroll, BorderLayout.CENTER);

        JButton newPost = brandButton("New Post", BRAND_TEAL, COLOR_WHITE);
        forumPanel.add(newPost, BorderLayout.SOUTH);
        rightColumn.add(forumPanel);

        // ---------- (4) UPCOMING TRIPS ----------
        JPanel tripsPanel = new JPanel(new BorderLayout());
        styleCardPanel(tripsPanel, "Upcoming Trips");

        String[] columns = {"Destination", "Dates", "Status"};
        Object[][] data = {
            {"Mumbai", "2024-02-12", "Confirmed"},
            {"Goa", "2024-03-01", "Pending"},
            {"Shimla", "2024-04-05", "Booked"}
        };

        JTable table = new JTable(data, columns);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(FONT_POPPINS_PLAIN_12);
        table.setRowHeight(25);
        table.getTableHeader().setFont(FONT_POPPINS_BOLD_14);
        table.getTableHeader().setBackground(BRAND_TEAL);
        table.getTableHeader().setForeground(COLOR_WHITE);

        JScrollPane tableScroll = new JScrollPane(table);
        tripsPanel.add(tableScroll, BorderLayout.CENTER);
        rightColumn.add(tripsPanel);

        setVisible(true);
    }

    // ---------- HELPER STYLING ----------
    private void styleCardPanel(JPanel panel, String title) {
        panel.setBackground(CARD_BG);
        Border line = BorderFactory.createLineBorder(BORDER_COLOR);
        Border inner = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        TitledBorder titled = BorderFactory.createTitledBorder(line, title, TitledBorder.LEFT, TitledBorder.TOP, FONT_POPPINS_BOLD_16, TEXT_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(titled, inner));
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND_BLUE);
        b.setForeground(new Color(230, 240, 255));
        b.setFont(FONT_POPPINS_BOLD_14);
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
        b.setFont(FONT_POPPINS_BOLD_14);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Log Out":
                dispose();
                new Login().setVisible(true);
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Opening " + cmd + "...");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("Abhinav").setVisible(true));
    }
}
