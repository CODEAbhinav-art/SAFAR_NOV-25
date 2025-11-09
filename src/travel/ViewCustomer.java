package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewCustomer extends JFrame implements ActionListener {

    private JButton backButton, editButton;
    private JLabel nameValue, emailValue, phoneValue, addressValue, passwordValue, usernameValue;

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color CONTENT_BG = new Color(250, 251, 253);
    private static final Color CARD_BG = new Color(245, 247, 250);
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Color TEXT_LIGHT = new Color(90, 100, 120);

    private static final Font FONT_BOLD_22 = new Font("Poppins", Font.BOLD, 22);
    private static final Font FONT_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private String username;

    public ViewCustomer(String username) {
        this.username = username == null ? "guest" : username;
        setTitle("Safar - View Personal Details");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ---------- LEFT PANEL (Brand Side) ----------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 600));
        leftPanel.setBackground(BRAND_BLUE);

        JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Your Profile</span></center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ---------- RIGHT PANEL (Info) ----------
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

        // ---------- Info Fields ----------
        int startY = 130;
        int gap = 45;

        usernameValue = addInfoField("Username", username, 60, startY, infoPanel);
        nameValue = addInfoField("Full Name", "Abhinav Mishra", 60, startY + gap, infoPanel);
        emailValue = addInfoField("Email Address", "abhinav@example.com", 60, startY + 2 * gap, infoPanel);
        phoneValue = addInfoField("Phone Number", "+91 9876543210", 60, startY + 3 * gap, infoPanel);
        addressValue = addInfoField("Address", "Sector 21, Noida, Uttar Pradesh", 60, startY + 4 * gap, infoPanel);
        passwordValue = addInfoField("Password", "••••••••••", 60, startY + 5 * gap, infoPanel);

        // ---------- Buttons ----------
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

        // ---------- Footer ----------
        JLabel footer = new JLabel("Safar Travel Platform © 2025", SwingConstants.CENTER);
        footer.setFont(new Font("Poppins", Font.ITALIC, 12));
        footer.setForeground(TEXT_LIGHT);
        footer.setBounds(60, startY + 8 * gap, 400, 30);
        infoPanel.add(footer);

        setVisible(true);
    }

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
        if (e.getSource() == backButton) {
            dispose();
            new Dashboard(username).setVisible(true);
        } 
        else if (e.getSource() == editButton) {
            dispose();
            new UpdateCustomer(username).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewCustomer("Abhinav"));
    }
}
