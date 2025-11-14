package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Login Page - Safar Travel Platform
 * ---------------------------------
 * Authenticates user credentials using MySQL database
 * and opens the Dashboard on success.
 */
public class Login extends JFrame implements ActionListener {

    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, forgotButton, adminButton;
    private JLabel backIcon;

    // Theme colors for consistency
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);

    public Login() {
        // ----- Frame Settings -----
        setTitle("Safar - Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // ----- Left Panel (Branding Section) -----
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(320, 500));
        leftPanel.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("✈️ SAFAR by GSV", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ----- Right Panel (Login Form) -----
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Back navigation icon
        backIcon = new JLabel("←");
        backIcon.setFont(new Font("Poppins", Font.BOLD, 26));
        backIcon.setForeground(BRAND_BLUE);
        backIcon.setBounds(400, 10, 50, 40);
        backIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new ModeSelection(); // Go back to mode selection page
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backIcon.setForeground(new Color(0x1C3C80)); // darker on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backIcon.setForeground(BRAND_BLUE);
            }
        });

        formPanel.add(backIcon);

        // ----- Title -----
        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setForeground(new Color(34, 45, 65));
        title.setBounds(60, 50, 400, 40);
        formPanel.add(title);

        JLabel subtitle = new JLabel("Log into your account");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(60, 90, 300, 25);
        formPanel.add(subtitle);

        // ----- Username Field -----
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        userLabel.setBounds(60, 150, 100, 25);
        formPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(60, 180, 320, 40);
        usernameField.setFont(new Font("Poppins", Font.PLAIN, 14));
        usernameField.setBackground(new Color(240, 240, 240));
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        formPanel.add(usernameField);

        // ----- Password Field -----
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        passLabel.setBounds(60, 230, 100, 25);
        formPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 260, 320, 40);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 14));
        passwordField.setBackground(new Color(240, 240, 240));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        formPanel.add(passwordField);

        // ----- Buttons -----
        loginButton = createButton("Login", BRAND_BLUE, Color.WHITE, 60, 320, 150, 45, formPanel);
        signupButton = createButton("Sign Up", BRAND_TEAL, Color.WHITE, 230, 320, 150, 45, formPanel);
        forgotButton = createButton("Forgot Password?", Color.WHITE, BRAND_BLUE, 60, 380, 160, 30, formPanel);
        adminButton = createButton("Admin Login", Color.WHITE, BRAND_TEAL, 230, 380, 150, 30, formPanel);

        forgotButton.setBorder(BorderFactory.createEmptyBorder());
        adminButton.setBorder(BorderFactory.createEmptyBorder());

        setVisible(true);
    }

    /**
     * Helper method to create a button with consistent style
     */
    private JButton createButton(String text, Color bg, Color fg, int x, int y, int w, int h, JPanel parent) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        parent.add(btn);
        return btn;
    }

    /**
     * Event Handler - Handles button clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == signupButton) {
            new Signup().setVisible(true);
            this.dispose();
        } 
        else if (src == loginButton) {
            authenticateUser();
        } 
        else if (src == forgotButton) {
            JOptionPane.showMessageDialog(this, "Password recovery feature coming soon!");
        } 
        else if (src == adminButton) {
            this.dispose();
            new AdminLogin().setVisible(true);
        }
    }

    /**
     * Authenticate user credentials from MySQL
     */
    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Basic empty check
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "❌ Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query user
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Success
                JOptionPane.showMessageDialog(this, "✅ Welcome back, " + rs.getString("fullname") + "!");
                new Dashboard(username).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
