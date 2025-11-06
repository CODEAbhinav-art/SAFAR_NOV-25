package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame implements ActionListener {

    private JTextField usernameEmailField;
    private JPasswordField passwordField;
    private JButton loginButton, backToUserButton;

    public AdminLogin() {
        setTitle("Travel Planner - Admin Login");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Panel ====
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // ==== Left Blue Panel ====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(320, 550));
        leftPanel.setBackground(new Color(0x2451A6));
        JLabel logo = new JLabel("Admin Access ✈️", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ==== Right Form Area ====
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Welcome Back, Admin!");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(new Color(34, 45, 65));
        title.setBounds(60, 60, 400, 40);
        formPanel.add(title);

        JLabel subtitle = new JLabel("Login to your admin dashboard");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(60, 100, 400, 25);
        formPanel.add(subtitle);

        // Username/Email
        addLabel("Username or Email", 60, 160, formPanel);
        usernameEmailField = addTextField(60, 185, 320, 40, formPanel);

        // Password
        addLabel("Password", 60, 245, formPanel);
        passwordField = addPasswordField(60, 270, 320, 40, formPanel);

        // Buttons
        loginButton = createButton("Login", new Color(0x2451A6), Color.WHITE, 60, 340, 150, 45, formPanel);
        backToUserButton = createButton("Switch to User Login", new Color(0x2C7A7B), Color.WHITE, 230, 340, 180, 45, formPanel);

        // Footer
        JLabel footer = new JLabel("For authorized personnel only", SwingConstants.CENTER);
        footer.setFont(new Font("Poppins", Font.ITALIC, 12));
        footer.setForeground(new Color(130, 130, 130));
        footer.setBounds(60, 400, 350, 30);
        formPanel.add(footer);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.PLAIN, 14));
        label.setForeground(new Color(80, 80, 80));
        label.setBounds(x, y, 200, 25);
        panel.add(label);
    }

    private JTextField addTextField(int x, int y, int w, int h, JPanel panel) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(new Font("Poppins", Font.PLAIN, 14));
        tf.setBackground(new Color(240, 240, 240));
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(tf);
        return tf;
    }

    private JPasswordField addPasswordField(int x, int y, int w, int h, JPanel panel) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y, w, h);
        pf.setFont(new Font("Poppins", Font.PLAIN, 14));
        pf.setBackground(new Color(240, 240, 240));
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
        if (e.getSource() == loginButton) {
            JOptionPane.showMessageDialog(this, "Admin login successful! (Demo)");
             new AdminDashboard("").setVisible(true);
             this.dispose();
        } else if (e.getSource() == backToUserButton) {
            new travel.Login().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}


