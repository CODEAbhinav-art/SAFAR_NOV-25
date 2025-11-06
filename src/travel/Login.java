package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, forgotButton, adminButton;

    public Login() {
        // Basic window setup
        setTitle("Travel Planner - Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Panel ====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // ==== Left (Logo or Image) ====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0x2451A6)); // deep blue
        leftPanel.setPreferredSize(new Dimension(320, 500));
        leftPanel.setLayout(new BorderLayout());

        JLabel logo = new JLabel("✈️ SAFAR by GSV", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ==== Right (Login Form) ====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);

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

        // ==== Buttons ====
        loginButton = createButton("Login", new Color(0x2451A6), Color.WHITE, 60, 320, 150, 45, formPanel);
        signupButton = createButton("Sign Up", new Color(0x2C7A7B), Color.WHITE, 230, 320, 150, 45, formPanel);
        forgotButton = createButton("Forgot Password?", Color.WHITE, new Color(0x2451A6), 60, 380, 160, 30, formPanel);
        adminButton = createButton("Admin Login", Color.WHITE, new Color(0x2C7A7B), 230, 380, 150, 30, formPanel);
        forgotButton.setBorder(BorderFactory.createEmptyBorder());
        adminButton.setBorder(BorderFactory.createEmptyBorder());

        setVisible(true);
    }

    private JButton createButton(String text, Color bg, Color fg, int x, int y, int w, int h, JPanel parent) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(this);
        parent.add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
          
            new Signup().setVisible(true);
            this.setVisible(false);
        } else if (e.getSource() == loginButton) {
            JOptionPane.showMessageDialog(this, "Welcome User");
            this.setVisible(false);
            new Dashboard("User").setVisible(true);
            
        } else if (e.getSource() == forgotButton) {
            JOptionPane.showMessageDialog(this, "Forgot Password clicked!");
        } else if (e.getSource() == adminButton) {
            this.setVisible(false);
            new AdminLogin().setVisible(true);
        }
    }

    
}
