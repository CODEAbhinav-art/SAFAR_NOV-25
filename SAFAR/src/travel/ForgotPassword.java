package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPassword extends JFrame implements ActionListener {

    private JTextField emailField;
    private JPasswordField newPasswordField, confirmPasswordField;
    private JButton resetButton, backButton, sendOtpButton;

    // Theme colors
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color FIELD_BG = new Color(245, 247, 250);

    public ForgotPassword() {
        setTitle("Safar - Forgot Password");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ==== LEFT PANEL ====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 550));
        leftPanel.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("<html><center>Forgot<br>Password 🔒</center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ==== RIGHT PANEL ====
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        add(formPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Reset Your Password");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setBounds(60, 50, 400, 40);
        formPanel.add(title);

        JLabel subtitle = new JLabel("Enter your registered email to receive reset instructions");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setBounds(60, 85, 500, 25);
        formPanel.add(subtitle);

        // --- Email Field ---
        addLabel("Registered Email", 60, 140, formPanel);
        emailField = addTextField(60, 165, 380, 40, formPanel);

        // --- OTP / Verify Button (conceptual only) ---
        sendOtpButton = createButton("Send OTP", BRAND_TEAL, Color.WHITE, 460, 165, 120, 40, formPanel);

        // --- New Password Fields ---
        addLabel("New Password", 60, 230, formPanel);
        newPasswordField = addPasswordField(60, 255, 380, 40, formPanel);

        addLabel("Confirm Password", 60, 315, formPanel);
        confirmPasswordField = addPasswordField(60, 340, 380, 40, formPanel);

        // --- Buttons ---
        resetButton = createButton("Reset Password", BRAND_BLUE, Color.WHITE, 60, 410, 180, 45, formPanel);
        backButton = createButton("Back to Login", BRAND_TEAL, Color.WHITE, 260, 410, 180, 45, formPanel);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.PLAIN, 14));
        label.setForeground(TEXT_LIGHT);
        label.setBounds(x, y, 200, 25);
        panel.add(label);
    }

    private JTextField addTextField(int x, int y, int w, int h, JPanel panel) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(new Font("Poppins", Font.PLAIN, 14));
        tf.setBackground(FIELD_BG);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(tf);
        return tf;
    }

    private JPasswordField addPasswordField(int x, int y, int w, int h, JPanel panel) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y, w, h);
        pf.setFont(new Font("Poppins", Font.PLAIN, 14));
        pf.setBackground(FIELD_BG);
        pf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(pf);
        return pf;
    }

    private JButton createButton(String text, Color bg, Color fg, int x, int y, int w, int h, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(this);
        panel.add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            new Login().setVisible(true);
            this.dispose();
        } else if (e.getSource() == sendOtpButton) {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your registered email first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Simulated OTP send
            JOptionPane.showMessageDialog(this, "✅ OTP has been sent to " + email + ". (Demo mode)");
        } else if (e.getSource() == resetButton) {
            String newPass = new String(newPasswordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both password fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "✅ Password reset successful!\nYou can now log in with your new password.");
            new Login().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForgotPassword().setVisible(true));
    }
}
