package travel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.JTableHeader;

public class Dashboard extends JFrame implements ActionListener { 

    // --- Theme Colors (from AdminDashboard) ---
    private static final Color BRAND_BLUE = new Color(0x2451A6); 
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250); // Using this for cards
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Color ACCENT_ORANGE = new Color(255, 87, 34); // Kept your orange accent
    private static final Color ACCENT_RED = new Color(220, 53, 69);

    // --- Fonts ---
    private static final Font FONT_POPPINS_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_POPPINS_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_POPPINS_BOLD_14 = new Font("Poppins", Font.BOLD, 14);
    private static final Font FONT_POPPINS_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);
    private static final Font FONT_POPPINS_PLAIN_12 = new Font("Poppins", Font.PLAIN, 12);

    String username;

    Dashboard(String username) {
        this.username = username;
        setTitle("Travel Planner - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 840); // Increased size to fit all content
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        // ---------- 1. LEFT NAVIGATION PANEL ----------
        JPanel leftNavPanel = new JPanel(new BorderLayout());
        leftNavPanel.setPreferredSize(new Dimension(300, 0));
        leftNavPanel.setBackground(BRAND_BLUE);
        leftNavPanel.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));

        // Logo/Title Area
        JLabel logo = new JLabel("<html><center>Travel Planner<br/><span style='font-size:12px'>Your Journey Awaits</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 24));
        logo.setForeground(COLOR_WHITE);
        leftNavPanel.add(logo, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(Box.createVerticalStrut(40)); // Add spacing at the top

        String[] buttonLabels = {
            "Update Personal Details", "View Personal Details", "Check Package",
            "Book Package", "View Package", "Book Ticket", "View Booked Ticket"
        };

        for (String label : buttonLabels) {
            JButton button = createNavButton(label);
            button.addActionListener(this);
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createVerticalStrut(10)); // Spacing between buttons
        }
        
        buttonsPanel.add(Box.createVerticalGlue()); // Pushes buttons up
        leftNavPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Log Out Button (at the bottom)
        JButton logoutButton = brandButton("Log Out", ACCENT_RED, COLOR_WHITE);
        logoutButton.addActionListener(this);
        leftNavPanel.add(logoutButton, BorderLayout.SOUTH);

        add(leftNavPanel, BorderLayout.WEST);

        // ---------- 2. RIGHT CONTENT PANEL ----------
        JPanel contentPanel = new JPanel(new BorderLayout(18, 18));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(contentPanel, BorderLayout.CENTER);

        // Header Area
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel welcomeLabel = new JLabel("Hello Traveler, " + (username.isEmpty() ? "Welcome!" : username + "!"));
        welcomeLabel.setFont(FONT_POPPINS_BOLD_22);
        welcomeLabel.setForeground(TEXT_DARK);
        headerPanel.add(welcomeLabel);
        
        JLabel contentLabel1 = new JLabel("Welcome to your Travel Planner Dashboard. Your journey begins here.");
        contentLabel1.setFont(FONT_POPPINS_PLAIN_14);
        contentLabel1.setForeground(TEXT_LIGHT);
        headerPanel.add(contentLabel1);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Content Area (2-column layout)
        JPanel mainContentGrid = new JPanel(new GridLayout(1, 2, 18, 18));
        mainContentGrid.setOpaque(false);

        // Left Column
        JPanel leftColumn = new JPanel(new GridLayout(3, 1, 0, 18));
        leftColumn.setOpaque(false);
        
        // Right Column
        JPanel rightColumn = new JPanel(new GridLayout(2, 1, 0, 18));
        rightColumn.setOpaque(false);

        mainContentGrid.add(leftColumn);
        mainContentGrid.add(rightColumn);
        contentPanel.add(mainContentGrid, BorderLayout.CENTER);
        

        // --- Populating the Content Panels ---

        // Panel: Dynamic Travel Map (Slideshow)
        JPanel mapPanel = new JPanel(new BorderLayout());
        styleCardPanel(mapPanel, "Dynamic Travel Map");
        // NOTE: Update these image paths for the slideshow to work!
        String[] imagePaths = {"travel/icons/GSV_Palace.jpg", "travel/icons/GSV_Logo.png", "travel/icons/Login_pic.jpg"};
        ImageSlideshow slideshow = new ImageSlideshow(imagePaths);
        mapPanel.add(slideshow, BorderLayout.CENTER);
        leftColumn.add(mapPanel);
        

        // Panel: Travel Journal
        JPanel journalPanel = new JPanel();
        styleCardPanel(journalPanel, "Travel Journal");
        journalPanel.setLayout(new BoxLayout(journalPanel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < 3; i++) {
            JPanel entryItem = new JPanel();
            entryItem.setBackground(CONTENT_BG); // Lighter background for entry
            entryItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            entryItem.setLayout(new BoxLayout(entryItem, BoxLayout.Y_AXIS));
            entryItem.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel dateLabel = new JLabel("Date: 2023-10-01");
            dateLabel.setForeground(TEXT_LIGHT);
            dateLabel.setFont(FONT_POPPINS_PLAIN_12);

            JLabel locationLabel = new JLabel("Location: Anuradhapura, Sri Lanka");
            locationLabel.setForeground(TEXT_DARK);
            locationLabel.setFont(FONT_POPPINS_BOLD_14);

            JLabel entryTextLabel = new JLabel("Enjoyed my time exploring the beautiful city!");
            entryTextLabel.setForeground(TEXT_LIGHT);
            entryTextLabel.setFont(FONT_POPPINS_PLAIN_12);

            entryItem.add(locationLabel);
            entryItem.add(dateLabel);
            entryItem.add(entryTextLabel);

            journalPanel.add(entryItem);
            journalPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Spacing
        }
        leftColumn.add(journalPanel);
        

        // Panel: Travel Budget Tracker
        JPanel budgetPanel = new JPanel();
        styleCardPanel(budgetPanel, "Travel Budget Tracker");
        budgetPanel.setLayout(new BoxLayout(budgetPanel, BoxLayout.Y_AXIS));

        JLabel budgetLabel = new JLabel("Budget Remaining: Rs 47,500.00");
        budgetLabel.setForeground(TEXT_DARK);
        budgetLabel.setFont(FONT_POPPINS_BOLD_16);
        budgetPanel.add(budgetLabel);
        budgetPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(70);
        progressBar.setStringPainted(true);
        progressBar.setForeground(BRAND_TEAL);
        progressBar.setBackground(CONTENT_BG);
        budgetPanel.add(progressBar);
        budgetPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel expenseLabel = new JLabel("Recent Expenses:");
        expenseLabel.setForeground(TEXT_DARK);
        expenseLabel.setFont(FONT_POPPINS_BOLD_14);
        budgetPanel.add(expenseLabel);

        JLabel expenseItem1 = new JLabel("• Dinner - Rs 500.00");
        expenseItem1.setForeground(TEXT_LIGHT);
        expenseItem1.setFont(FONT_POPPINS_PLAIN_12);
        budgetPanel.add(expenseItem1);

        JLabel expenseItem2 = new JLabel("• Taxi - Rs 200.00");
        expenseItem2.setForeground(TEXT_LIGHT);
        expenseItem2.setFont(FONT_POPPINS_PLAIN_12);
        budgetPanel.add(expenseItem2);
        
        budgetPanel.add(Box.createVerticalGlue()); // Pushes button to bottom

        JButton addExpenseButton = brandButton("Add Expense", ACCENT_ORANGE, COLOR_WHITE);
        addExpenseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        budgetPanel.add(addExpenseButton);
        leftColumn.add(budgetPanel);

        // Panel: Travel Community Forum
        JPanel forumPanel = new JPanel(new BorderLayout(10, 10));
        styleCardPanel(forumPanel, "Travel Community Forum");

        JTextArea postsArea = new JTextArea();
        postsArea.setEditable(false);
        postsArea.setBackground(CONTENT_BG);
        postsArea.setForeground(TEXT_DARK);
        postsArea.setFont(FONT_POPPINS_PLAIN_12);
        postsArea.setLineWrap(true);
        postsArea.setWrapStyleWord(true);
        
        // Add dummy forum posts
        for (int i = 0; i < 5; i++) {
            postsArea.append("Susith Deshan Alwis\n");
            postsArea.append("Title: Best Restaurants in Anuradhapura\n");
            postsArea.append("Looking for recommendations on great restaurants!\n");
            postsArea.append("----------------------------------------------------------\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(postsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        forumPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton newPostButton = brandButton("New Post", BRAND_TEAL, COLOR_WHITE);
        newPostButton.addActionListener(this);
        forumPanel.add(newPostButton, BorderLayout.SOUTH);
        rightColumn.add(forumPanel);


        // Panel: Upcoming Trips
        JPanel upcomingTripsPanel = new JPanel(new BorderLayout());
        styleCardPanel(upcomingTripsPanel, "Upcoming Trips");

        String[] columnNames = {"Destination", "Travel Dates", "Status"};
        Object[][] tripData = {
            {"Anuradhapura, Sri Lanka", "2023-10-15", "Booked"},
            {"Polonnaruwa, Sri Lanka", "2023-11-05", "Confirmed"},
            {"Kandy, Sri Lanka", "2024-01-20", "Pending"},
            {"Galle, Sri Lanka", "2024-01-20", "Confirmed"},
            {"Mathara, Sri Lanka", "2024-01-20", "Confirmed"}
        };

        JTable tripTable = new JTable(tripData, columnNames);
        tripTable.setBackground(CARD_BG);
        tripTable.setForeground(TEXT_DARK);
        tripTable.setFont(FONT_POPPINS_PLAIN_12);
        tripTable.setRowHeight(25);
        tripTable.getTableHeader().setFont(FONT_POPPINS_BOLD_14);
        tripTable.getTableHeader().setBackground(BRAND_TEAL);
        tripTable.getTableHeader().setForeground(COLOR_WHITE);

        JScrollPane tripScrollPane = new JScrollPane(tripTable);
        tripScrollPane.getViewport().setBackground(CARD_BG);
        upcomingTripsPanel.add(tripScrollPane, BorderLayout.CENTER);
        rightColumn.add(upcomingTripsPanel);
        
        // Finalize
        setVisible(true);
    }
    
    /**
     * Helper method to style a content panel as a "card".
     */
    private void styleCardPanel(JPanel panel, String title) {
        panel.setBackground(CARD_BG);
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR);
        Border emptyBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            lineBorder, title, TitledBorder.LEFT, TitledBorder.TOP, 
            FONT_POPPINS_BOLD_16, TEXT_DARK
        );
        
        panel.setBorder(BorderFactory.createCompoundBorder(titledBorder, emptyBorder));
    }

    /**
     * Helper method to create a styled navigation button for the left panel.
     */
    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND_BLUE);
        b.setForeground(new Color(230, 240, 255)); // Slightly off-white
        b.setFont(FONT_POPPINS_BOLD_14);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Basic hover effect
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(0x3366CC)); // Lighter blue
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(BRAND_BLUE);
            }
        });
        return b;
    }

    /**
     * Helper method to create a styled "brand" button (like in AdminDashboard).
     */
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
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        switch (command) {
            case "Update Personal Details": // Fixed from "Add Personal Details"
                // new AddCustomer().setVisible(true); // Assuming this is the correct class
                JOptionPane.showMessageDialog(this, "Opening 'Update Personal Details'...");
                break;
            case "View Personal Details":
                // new ViewCustomer(username).setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'View Personal Details'...");
                break;
            case "Check Package":
                // new CheckPackage().setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'Check Package'...");
                break;
            case "Book Package":
                // new BookPackage(username).setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'Book Package'...");
                break;
            case "View Package":
                // new ViewPackage(username).setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'View Package'...");
                break;
            case "Book Ticket":
                // new BookHotel(username).setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'Book Tickets'...");
                break;
            case "View Booked Tickets":
                // new ViewBookedHotel(username).setVisible(true);
                JOptionPane.showMessageDialog(this, "Opening 'View Booked Tickets'...");
                break;
            case "New Post":
                JOptionPane.showMessageDialog(this, "Opening 'New Post' dialog...");
                break;
            case "Log Out":
                this.dispose();
                 new Login().setVisible(true);
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
                break;
        }
    }
    
    // --- ImageSlideshow Inner Class (Unchanged) ---
    class ImageSlideshow extends JPanel implements ActionListener {
        private Timer timer;
        private int currentIndex;
        private String[] imagePaths;

        ImageSlideshow(String[] imagePaths) {
            this.imagePaths = imagePaths;
            this.currentIndex = 0;
            this.timer = new Timer(3000, this);
            this.timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Handle image loading gracefully
            ImageIcon imageIcon = new ImageIcon(imagePaths[currentIndex]);
            Image image = imageIcon.getImage();
            
            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Draw a placeholder if image fails to load
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawString("Image not found: " + imagePaths[currentIndex], 20, getHeight() / 2);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentIndex = (currentIndex + 1) % imagePaths.length; 
            repaint();
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel for better component rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new Dashboard("SafarUser").setVisible(true));
    }
}