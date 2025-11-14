package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * UpdateCustomer.java
 * -------------------
 * Allows a logged-in user to edit and update their personal information.
 * Connected to MySQL through DBConnection.java
 *
 * Part of the SAFAR Travel Management System
 */
public class UpdateCustomer extends JFrame implements ActionListener {

    private JTextField nameField, emailField, phoneField, addressField;
    private JPasswordField passwordField;
    private JButton saveButton, cancelButton;
    private String username;

    // --- Theme constants for consistency ---
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Font FONT_BOLD_20 = new Font("Poppins", Font.BOLD, 20);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    /**
     * Constructor - takes logged-in username, loads data from DB
     */
    public UpdateCustomer(String username) {
        this.username = username;
        
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No user session found. Please log in again.");
            new Login().setVisible(true);
            dispose();
            return;
        }

        // Frame setup
        setTitle("Safar - Update Personal Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ---------- LEFT PANEL (Brand) ----------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 550));
        leftPanel.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Profile Update</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // ---------- RIGHT PANEL (Form) ----------
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        add(formPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Update Your Personal Details");
        title.setFont(FONT_BOLD_20);
        title.setForeground(TEXT_DARK);
        title.setBounds(50, 40, 400, 30);
        formPanel.add(title);

        // --- Fields with labels ---
        addLabel("Full Name", 50, 100, formPanel);
        nameField = addTextField(50, 125, 380, 35, formPanel);

        addLabel("Email Address", 50, 175, formPanel);
        emailField = addTextField(50, 200, 380, 35, formPanel);

        addLabel("Phone Number", 50, 250, formPanel);
        phoneField = addTextField(50, 275, 380, 35, formPanel);

        addLabel("Address", 50, 325, formPanel);
        addressField = addTextField(50, 350, 380, 35, formPanel);

        addLabel("Password", 50, 400, formPanel);
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 425, 380, 35);
        passwordField.setFont(FONT_PLAIN_14);
        passwordField.setBackground(new Color(245, 247, 250));
        passwordField.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        formPanel.add(passwordField);

        // --- Buttons ---
        saveButton = createButton("Save Changes", BRAND_TEAL, Color.WHITE);
        saveButton.setBounds(50, 480, 160, 40);
        saveButton.addActionListener(this);
        formPanel.add(saveButton);

        cancelButton = createButton("Cancel", Color.WHITE, BRAND_BLUE);
        cancelButton.setBounds(230, 480, 160, 40);
        cancelButton.setBorder(BorderFactory.createLineBorder(BRAND_BLUE));
        cancelButton.setForeground(BRAND_BLUE);
        cancelButton.addActionListener(this);
        formPanel.add(cancelButton);

        // Load existing user details into form
        loadUserData();

        setVisible(true);
    }

    /**
     * Fetch and load user data from database to prefill form fields
     */
    private void loadUserData() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("fullname"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                addressField.setText(rs.getString("address"));
                passwordField.setText(rs.getString("password"));
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a styled label to the form
     */
    private void addLabel(String text, int x, int y, JPanel parent) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_PLAIN_14);
        label.setForeground(TEXT_LIGHT);
        label.setBounds(x, y, 300, 25);
        parent.add(label);
    }

    /**
     * Creates a styled text field
     */
    private JTextField addTextField(int x, int y, int w, int h, JPanel parent) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(FONT_PLAIN_14);
        tf.setBackground(new Color(245, 247, 250));
        tf.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        parent.add(tf);
        return tf;
    }

    /**
     * Creates a consistent button style
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
     * Handle button actions: Save or Cancel
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            updateUserDetails();
        } 
        else if (e.getSource() == cancelButton) {
            dispose();
            new ViewCustomer(username).setVisible(true);
        }
    }

    /**
     * Validates and updates user details in the database
     */
    private void updateUserDetails() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String updateQuery = "UPDATE users SET fullname=?, email=?, phone=?, address=?, password=? WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, password);
            stmt.setString(6, username);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
                dispose();
                new ViewCustomer(username).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No changes made or user not found.", "Update Failed", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Entry point for testing independently
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateCustomer("testuser"));
    }
}
