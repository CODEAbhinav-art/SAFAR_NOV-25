package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModeSelection extends JFrame implements ActionListener {

    private JButton businessButton, personalButton;

    public ModeSelection() {
        setTitle("Safar - Choose Mode");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Layout ====
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // ==== Left Blue Branding Panel ====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(320, 550));
        leftPanel.setBackground(new Color(0x2451A6));

        JLabel logo = new JLabel("<html><div style='text-align:center;'>Welcome to<br>Safar ✈️</div></html>", SwingConstants.CENTER);
        logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ==== Right Option Panel ====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Choose Your Mode");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(new Color(34, 45, 65));
        title.setBounds(60, 80, 400, 40);
        rightPanel.add(title);

        JLabel subtitle = new JLabel("How would you like to use Safar?");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(60, 120, 350, 25);
        rightPanel.add(subtitle);

        // ==== Business Button ====
        businessButton = createButton("Business Mode (For Agencies)", new Color(0x2451A6), Color.WHITE, 60, 190, 300, 50, rightPanel);

        // ==== Personal Button ====
        personalButton = createButton("Personal Mode (For Travelers)", new Color(0x2C7A7B), Color.WHITE, 60, 260, 300, 50, rightPanel);

        // ==== Footer ====
        JLabel footer = new JLabel("Safar v1.0  |  © 2025 Team Safar", SwingConstants.CENTER);
        footer.setFont(new Font("Poppins", Font.ITALIC, 12));
        footer.setForeground(new Color(120, 120, 120));
        footer.setBounds(60, 430, 350, 30);
        rightPanel.add(footer);

        setVisible(true);
    }

    private JButton createButton(String text, Color bg, Color fg, int x, int y, int w, int h, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        panel.add(btn);

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == businessButton) {
            JOptionPane.showMessageDialog(this, "Launching Business Mode...");
            // TODO: Launch AdminLogin or BusinessDashboard
            
        } else if (e.getSource() == personalButton) {
            JOptionPane.showMessageDialog(this, "Launching Personal Mode...");
            // TODO: Launch User Login or main dashboard for travelers
            new travel.Login();
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new ModeSelection().setVisible(true);;
    }
}
