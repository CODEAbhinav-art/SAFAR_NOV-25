package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ViewCustomer.java
 * ------------------
 * Displays the personal details of the currently logged-in user
 * by fetching information from the Safar MySQL database.
 *
 * Part of the SAFAR Travel Management System.
 */
public class ViewCustomer extends JFrame implements ActionListener {

    // --- UI Components ---
    private JButton backButton, editButton;
    private JLabel nameValue, emailValue, phoneValue, addressValue, passwordValue, usernameValue;

    // --- Theme Colors ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);

    // --- Fonts ---
    private static final Font FONT_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private String username;

    /**
     * Constructor - takes logged-in username as argument.
     */
    public ViewCustomer(String username) {
    	this.username = username != null && !username.trim().isEmpty() ? username : null;
    	
    	if (username == null || username.trim().isEmpty()) {
    	    JOptionPane.showMessageDialog(this, "No user session found. Please log in again.");
    	    new Login().setVisible(true);
    	    dispose();
    	    return;
    	}

        // --- Frame Setup ---
        setTitle("Safar - View Personal Details");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // --- Left Branding Panel ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 600));
        leftPanel.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Your Profile</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // --- Right Content Panel ---
        JPanel infoPanel = new JPanel(null);
        infoPanel.setBackground(CONTENT_BG);
        add(infoPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Your Personal Information");
        title.setFont(FONT_BOLD_22);
        title.setForeground(TEXT_DARK);
        title.setBounds(60, 40, 400, 30);
        infoPanel.add(title);

        JLabel subtitle = new JLabel("Review your stored Safar profile details");
        subtitle.setFont(FONT_PLAIN_14);
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setBounds(60, 75, 400, 25);
        infoPanel.add(subtitle);

        // --- Information Fields (Empty initially, filled after DB fetch) ---
        int startY = 130;
        int gap = 45;

        usernameValue = addInfoField("Username", "", 60, startY, infoPanel);
        nameValue = addInfoField("Full Name", "", 60, startY + gap, infoPanel);
        emailValue = addInfoField("Email Address", "", 60, startY + 2 * gap, infoPanel);
        phoneValue = addInfoField("Phone Number", "", 60, startY + 3 * gap, infoPanel);
        addressValue = addInfoField("Address", "", 60, startY + 4 * gap, infoPanel);
        passwordValue = addInfoField("Password", "••••••••", 60, startY + 5 * gap, infoPanel);

        // --- Action Buttons ---
        editButton = createButton("Edit Details", BRAND_TEAL, Color.WHITE);
        editButton.setBounds(60, startY + 6 * gap + 10, 160, 45);
        editButton.addActionListener(this);
        infoPanel.add(editButton);

        backButton = createButton("Back", Color.WHITE, BRAND_BLUE);
        backButton.setBounds(240, startY + 6 * gap + 10, 160, 45);
        backButton.setBorder(BorderFactory.createLineBorder(BRAND_BLUE));
        backButton.setForeground(BRAND_BLUE);
        backButton.addActionListener(this);
        infoPanel.add(backButton);

        JLabel footer = new JLabel("Safar Travel Platform © 2025", SwingConstants.CENTER);
        footer.setFont(new Font("Poppins", Font.ITALIC, 12));
        footer.setForeground(TEXT_LIGHT);
        footer.setBounds(60, startY + 8 * gap, 400, 30);
        infoPanel.add(footer);

        // --- Fetch user data from MySQL ---
        loadUserDetails();

        setVisible(true);
    }

    /**
     * Fetch user details from the database using the username.
     */
    private void loadUserDetails() {
        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "❌ Unable to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Populate user details into labels
                usernameValue.setText(rs.getString("username"));
                nameValue.setText(rs.getString("fullname"));
                emailValue.setText(rs.getString("email"));
                phoneValue.setText(rs.getString("phone"));
                addressValue.setText(rs.getString("address"));

                // Hide the actual password
                passwordValue.setText("••••••••");
            } else {
                JOptionPane.showMessageDialog(this, "User not found in database.", "Not Found", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a label pair (Field name + value) to the panel.
     */
    private JLabel addInfoField(String label, String value, int x, int y, JPanel panel) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(FONT_BOLD_16);
        fieldLabel.setForeground(TEXT_DARK);
        fieldLabel.setBounds(x, y, 150, 25);
        panel.add(fieldLabel);

        JLabel fieldValue = new JLabel(value);
        fieldValue.setFont(FONT_PLAIN_14);
        fieldValue.setForeground(TEXT_LIGHT);
        fieldValue.setBounds(x + 160, y, 400, 25);
        panel.add(fieldValue);

        return fieldValue;
    }

    /**
     * Creates styled buttons used throughout Safar UI.
     */
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Handles all button click actions.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            new Dashboard(username).setVisible(true);
        } 
        else if (e.getSource() == editButton) {
            dispose();
            new UpdateCustomer(username).setVisible(true);
        }
    }

    /**
     * Entry point for standalone testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCustomer("testuser"));
    }
}
