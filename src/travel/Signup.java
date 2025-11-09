package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Signup extends JFrame implements ActionListener {

    private JTextField usernameField, fullnameField, phoneField, addressField, emailField;
    private JPasswordField passwordField;
    private JButton createButton, backButton;

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color FIELD_BG = new Color(245, 247, 250);

    public Signup() {
        setTitle("Safar - Sign Up");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Panel ====
        JPanel mainPan = new JPanel(new BorderLayout());
        add(mainPan);

        // ==== Left Blue Panel ====
        JPanel leftPan = new JPanel(new BorderLayout());
        leftPan.setPreferredSize(new Dimension(320, 600));
        leftPan.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("<html><center>Join SAFAR<br>✈</center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        leftPan.add(logo, BorderLayout.CENTER);

        mainPan.add(leftPan, BorderLayout.WEST);

        // ==== Right Form Panel ====
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        mainPan.add(formPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setBounds(60, 40, 400, 40);
        formPanel.add(title);

        JLabel subtitle = new JLabel("Sign up to explore the world with SAFAR");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setBounds(60, 75, 400, 25);
        formPanel.add(subtitle);

        // --- Row 1: Full Name & Username ---
        addLabel("Full Name", 60, 130, formPanel);
        fullnameField = addTextField(60, 155, 320, 40, formPanel);

        addLabel("Username", 420, 130, formPanel);
        usernameField = addTextField(420, 155, 320, 40, formPanel);

        // --- Row 2: Email & Phone ---
        addLabel("Email Address", 60, 210, formPanel);
        emailField = addTextField(60, 235, 320, 40, formPanel);

        addLabel("Phone Number", 420, 210, formPanel);
        phoneField = addTextField(420, 235, 320, 40, formPanel);

        // --- Row 3: Address ---
        addLabel("Address", 60, 290, formPanel);
        addressField = addTextField(60, 315, 680, 40, formPanel);

        // --- Row 4: Password ---
        addLabel("Password", 60, 370, formPanel);
        passwordField = addPasswordField(60, 395, 320, 40, formPanel);

        // --- Buttons ---
        createButton = createButton("Create Account", BRAND_BLUE, Color.WHITE, 60, 460, 200, 45, formPanel);
        backButton = createButton("Back to Login", BRAND_TEAL, Color.WHITE, 280, 460, 200, 45, formPanel);

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
        } else if (e.getSource() == createButton) {
            // Validate user input
            String fullName = fullnameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // (Future: Insert into DB)
            JOptionPane.showMessageDialog(this,
                    "✅ Account Created Successfully!\nWelcome, " + fullName + "!");
            new Login().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new Signup().setVisible(true);
    }
}
