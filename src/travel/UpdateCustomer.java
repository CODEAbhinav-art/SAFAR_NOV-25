package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateCustomer extends JFrame implements ActionListener {

    private JTextField nameField, emailField, phoneField, addressField;
    private JPasswordField passwordField;
    private JButton saveButton, cancelButton;
    private String username;

    // Theme colors
    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);
    private static final Color BORDER_COLOR = new Color(220, 220, 225);
    private static final Font FONT_BOLD_20 = new Font("Poppins", Font.BOLD, 20);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    public UpdateCustomer(String username) {
        this.username = username;
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

        // --- Fields ---
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
        cancelButton.addActionListener(this);
        formPanel.add(cancelButton);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y, JPanel parent) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_PLAIN_14);
        label.setForeground(TEXT_LIGHT);
        label.setBounds(x, y, 300, 25);
        parent.add(label);
    }

    private JTextField addTextField(int x, int y, int w, int h, JPanel parent) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setFont(FONT_PLAIN_14);
        tf.setBackground(new Color(245, 247, 250));
        tf.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        parent.add(tf);
        return tf;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simulate database update (you can later connect to DB)
            JOptionPane.showMessageDialog(this, "✅ Details updated successfully!\n\nName: " + name + "\nEmail: " + email + "\nPhone: " + phone);
            dispose();
        } 
        else if (e.getSource() == cancelButton) {
        	new Dashboard("").setVisible(true);
            dispose();
           
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateCustomer("Abhinav"));
    }
}
