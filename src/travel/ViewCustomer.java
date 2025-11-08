package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewCustomer extends JFrame implements ActionListener {

    private JButton backButton;
    private JLabel usernameValue, idValue, idNumberValue, nameValue, genderValue;
    private JLabel countryValue, addressValue, phoneValue, emailValue;

    public ViewCustomer(String username) {
        setTitle("Travel Planner - View Customer");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Panel ====
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // ==== Left Blue Panel ====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 550));
        leftPanel.setBackground(new Color(0x2451A6));
        JLabel logo = new JLabel("<html><center>Customer<br>Details ✈️</center></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ==== Right Information Panel ====
        JPanel infoPanel = new JPanel(null);
        infoPanel.setBackground(Color.WHITE);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Customer Information");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(new Color(34, 45, 65));
        title.setBounds(60, 40, 400, 40);
        infoPanel.add(title);

        JLabel subtitle = new JLabel("Review customer profile details below");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(60, 75, 400, 25);
        infoPanel.add(subtitle);

        // ==== Labels and Fields ====
        int startY = 130;
        int gap = 40;

        usernameValue = addInfoField("Username", "john_doe", 60, startY, infoPanel);
        idValue = addInfoField("ID", "Passport", 60, startY + gap, infoPanel);
        idNumberValue = addInfoField("ID Number", "P12345678", 60, startY + 2 * gap, infoPanel);
        nameValue = addInfoField("Full Name", "John Doe", 60, startY + 3 * gap, infoPanel);
        genderValue = addInfoField("Gender", "Male", 60, startY + 4 * gap, infoPanel);
        countryValue = addInfoField("Country", "India", 60, startY + 5 * gap, infoPanel);
        addressValue = addInfoField("Address", "123, Mumbai Street", 60, startY + 6 * gap, infoPanel);
        phoneValue = addInfoField("Phone", "+91 9876543210", 60, startY + 7 * gap, infoPanel);
        emailValue = addInfoField("Email", "john@example.com", 60, startY + 8 * gap, infoPanel);

        // ==== Back Button ====
        backButton = new JButton("Back");
        backButton.setBounds(60, startY + 9 * gap + 10, 150, 45);
        backButton.setFont(new Font("Poppins", Font.BOLD, 14));
        backButton.setBackground(new Color(0x2451A6));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(this);
        infoPanel.add(backButton);

        // ==== Footer ====
        JLabel footer = new JLabel("Travel Management System © 2025", SwingConstants.CENTER);
        footer.setFont(new Font("Poppins", Font.ITALIC, 12));
        footer.setForeground(new Color(130, 130, 130));
        footer.setBounds(60, startY + 10 * gap, 400, 30);
        infoPanel.add(footer);

        setVisible(true);
    }

    private JLabel addInfoField(String label, String value, int x, int y, JPanel panel) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        fieldLabel.setForeground(new Color(60, 60, 60));
        fieldLabel.setBounds(x, y, 150, 25);
        panel.add(fieldLabel);

        JLabel fieldValue = new JLabel(value);
        fieldValue.setFont(new Font("Poppins", Font.PLAIN, 14));
        fieldValue.setForeground(new Color(90, 90, 90));
        fieldValue.setBounds(x + 160, y, 300, 25);
        panel.add(fieldValue);

        return fieldValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            this.dispose();
            new Dashboard("").setVisible(true);
        }
    }

    public static void main(String[] args) {
        new ViewCustomer("demo_user");
    }
}
