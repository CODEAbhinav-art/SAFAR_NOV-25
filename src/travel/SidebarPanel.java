package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SidebarPanel extends JPanel {
    private JButton[] buttons;
    private String[] buttonLabels = {
        "Dashboard",
        "Bookings",
        "Trips",
        "Profile",
        "Help",
        "Logout"
    };
    private Color baseColor = new Color(35, 40, 70);
    private Color hoverColor = new Color(255, 87, 34); // orange accent
    private Color textColor = new Color(230, 230, 230);
    private ActionListener parentListener;

    public SidebarPanel(ActionListener listener) {
        this.parentListener = listener;
        setLayout(new BorderLayout());
        setBackground(new Color(27, 31, 56)); // same as login background
        setPreferredSize(new Dimension(250, 0));

        // === LOGO AREA ===
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(27, 31, 56));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/travel/icons/GSV_Palace.jpg"));
        Image scaled = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("Travel Planner", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        logoPanel.add(logoLabel, BorderLayout.CENTER);
        logoPanel.add(titleLabel, BorderLayout.SOUTH);

        add(logoPanel, BorderLayout.NORTH);

        // === BUTTON AREA ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(27, 31, 56));
        buttonPanel.setLayout(new GridLayout(buttonLabels.length, 1, 0, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Poppins", Font.PLAIN, 14));
            buttons[i].setForeground(textColor);
            buttons[i].setBackground(baseColor);
            buttons[i].setFocusPainted(false);
            buttons[i].setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].addActionListener(listener);

            // Hover animation
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    e.getComponent().setBackground(baseColor);
                }
            });

            buttonPanel.add(buttons[i]);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }
}
