package travel;

import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {
    private JButton[] navButtons;
    private JPanel leftBrand, content;
    private String username;
    
    public AdminDashboard(String username) {
    	this.username = username; 
    	setTitle("SAFAR - Admin Dashboard");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 setSize(1100, 720);
    	 setLocationRelativeTo(null);
         setLayout(new BorderLayout());
         
         Color brandBlue = new Color(0x2451A56);
         Color brandTeal = new Color(0x2C7A7B);
         Color white = Color.WHITE;
         Color contentBg = new Color(250, 251, 253);
         Color cardBg = new Color(245, 247 , 250);
         Color textDark = new Color(34, 45, 65);
    	  
         leftBrand = new JPanel(new BorderLayout());
         leftBrand.setPreferredSize(new Dimension(320, 0));
         leftBrand.setBackground(brandBlue);
         leftBrand.setBorder(BorderFactory.createEmptyBorder(28, 20, 28, 20));
         
         JLabel logo = new JLabel("<html><center>SAFAR<br/><span style='font-size:12px'>Admin</span></center></html>", SwingConstants.CENTER);
         logo.setFont(new Font("Poppins", Font.BOLD, 28));
         logo.setForeground(white);
         leftBrand.add(logo, BorderLayout.NORTH);
         
         JPanel brandcenter = new JPanel();
         brandcenter.setOpaque(false);
         brandcenter.setLayout(new BoxLayout(brandcenter, BoxLayout.Y_AXIS));
         brandcenter.setBorder(BorderFactory.createEmptyBorder(20,6,20,6));
         
         JLabel headline = new JLabel("<html><span style='color:#ffffff'>Control center for</span><br/><b><span style='color:#ffffff'>Travel Planner</span></b></html>");
         headline.setFont(new Font("Poppins", Font.BOLD, 20));
         headline.setForeground(white);
         headline.setAlignmentX(Component.CENTER_ALIGNMENT);
         brandcenter.add(Box.createVerticalGlue());
         brandcenter.add(headline);
         brandcenter.add(Box.createVerticalStrut(12));
         
         JLabel sub = new JLabel("Manage users, packages and reports");
         sub.setFont(new Font("Poppins", Font.PLAIN, 13));
         sub.setForeground(new Color(230, 240, 255));
         sub.setAlignmentX(Component.CENTER_ALIGNMENT);
         brandcenter.add(sub);
         brandcenter.add(Box.createVerticalGlue());
         
         leftBrand.add(brandcenter, BorderLayout.CENTER);
         
         JPanel leftStats = new JPanel(new GridLayout(3, 1, 6, 6));
         leftStats.setOpaque(false);
         leftStats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
         JLabel s1 = makeStatLabel("Users", "1,245");
         JLabel s2 = makeStatLabel("Bookings", "370");
         JLabel s3 = makeStatLabel("Revenue", "₹4.2L");
         leftStats.add(s1);
         leftStats.add(s2);
         leftStats.add(s3);
         leftBrand.add(leftStats, BorderLayout.SOUTH);

         add(leftBrand, BorderLayout.WEST);
         
         //Right section starts here...
         content = new JPanel(new BorderLayout(18, 18));
         content.setBackground(contentBg);
         content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
         add(content, BorderLayout.CENTER);
         
         JPanel header = new JPanel(new BorderLayout(12, 12));
         header.setOpaque(false);
         JLabel title = new JLabel("Admin Dashboard");
         title.setFont(new Font("Poppins", Font.BOLD, 22));
         title.setForeground(textDark);
         header.add(title, BorderLayout.WEST);
         
         JButton primaryAction = brandButton("Create Package", brandBlue, white);
         primaryAction.addActionListener(e -> JOptionPane.showMessageDialog(this, "Create Package (placeholder)"));
         header.add(primaryAction, BorderLayout.EAST);
         
         content.add(header, BorderLayout.NORTH);
         
         JPanel center = new JPanel(new BorderLayout(16, 16));
         center.setOpaque(false);
         
      // Cards row (three cards)
         JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
         cards.setOpaque(false);
         cards.setPreferredSize(new Dimension(800, 140));
         cards.add(makeInfoCard("Total Users", "1,245", brandBlue));
         cards.add(makeInfoCard("Bookings", "370", brandTeal));
         cards.add(makeInfoCard("Revenue", "₹4.2L", new Color(255, 165, 64)));
         center.add(cards, BorderLayout.NORTH);
         
         JPanel lower = new JPanel(new GridLayout(1, 2, 16, 0));
         lower.setOpaque(false);  
         
         // Recent Activity (left)
         JPanel recent = new JPanel(new BorderLayout(8, 8));
         recent.setBackground(cardBg);
         recent.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createEmptyBorder(8, 8, 8, 8),
                 BorderFactory.createLineBorder(new Color(220, 220, 225))
         ));
         JLabel recTitle = new JLabel("Recent Activity");
         recTitle.setFont(new Font("Poppins", Font.BOLD, 14));
         recTitle.setForeground(textDark);
         recent.add(recTitle, BorderLayout.NORTH);
         
         DefaultListModel<String> lm = new DefaultListModel<>();
         lm.addElement("User 'ajay' created a bpoking.");
         lm.addElement("Package 'Island Explorer' updated.");
         lm.addElement("Payment received: ₹2,500.");
         lm.addElement("New support ticket from 'neha'.");
         JList<String> list = new JList<>(lm);
         list.setBackground(cardBg);
         list.setForeground(textDark);
         list.setFont(new Font("Poppins", Font.PLAIN, 13));
         recent.add(new JScrollPane(list), BorderLayout.CENTER);
         
         lower.add(recent);
         
         // Quick Actions (right)
         JPanel quick = new JPanel();
         quick.setLayout(new BoxLayout(quick, BoxLayout.Y_AXIS));
         quick.setBackground(cardBg);
         quick.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createEmptyBorder(12, 12, 12, 12),
                 BorderFactory.createLineBorder(new Color(220, 220, 225))
         ));

         JLabel qaTitle = new JLabel("Quick Actions");
         qaTitle.setFont(new Font("Poppins", Font.BOLD, 14));
         qaTitle.setForeground(textDark);
         quick.add(qaTitle);
         quick.add(Box.createVerticalStrut(12));

         JButton manageUsers = brandButton("Manage Users", brandBlue, white);
         JButton managePackages = brandButton("Manage Packages", brandTeal, white);
         JButton viewAnalytics = brandButton("View Analytics", new Color(255,165,64), white);
         manageUsers.setAlignmentX(Component.LEFT_ALIGNMENT);
         managePackages.setAlignmentX(Component.LEFT_ALIGNMENT);
         viewAnalytics.setAlignmentX(Component.LEFT_ALIGNMENT);

         manageUsers.addActionListener(this);
         managePackages.addActionListener(this);
         viewAnalytics.addActionListener(this);

         quick.add(manageUsers);
         quick.add(Box.createVerticalStrut(8));
         quick.add(managePackages);
         quick.add(Box.createVerticalStrut(8));
         quick.add(viewAnalytics);
         quick.add(Box.createVerticalGlue());

         lower.add(quick);
         

         center.add(lower, BorderLayout.CENTER);

         content.add(center, BorderLayout.CENTER);

         // bottom navigation area (logout + profile)
         JPanel bottomBar = new JPanel(new BorderLayout());
         bottomBar.setOpaque(false);

         JLabel userLabel = new JLabel("Signed in as: " + (username.isEmpty() ? "admin" : username));
         userLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
         userLabel.setForeground(textDark);
         bottomBar.add(userLabel, BorderLayout.WEST);

         JButton logout = brandButton("Logout", new Color(220, 53, 69), white);
         logout.addActionListener(e -> {
             dispose();
             SwingUtilities.invokeLater(() -> new AdminLogin().setVisible(true));
         });
         bottomBar.add(logout, BorderLayout.EAST);

         content.add(bottomBar, BorderLayout.SOUTH);
         
         setVisible(true);
    }
    
    private JPanel makeInfoCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255,255,255,230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,240)),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Poppins", Font.PLAIN, 14));
        t.setForeground(new Color(90, 100, 120));
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Poppins", Font.BOLD, 22));
        v.setForeground(accent);
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
    
    // small helper: stat label for left panel
    private JLabel makeStatLabel(String name, String value) {
        JLabel l = new JLabel("<html><b>" + value + "</b><br><small style='color:#e6f0ff'>" + name + "</small></html>");
        l.setForeground(new Color(230, 240, 255));
        l.setFont(new Font("Poppins", Font.PLAIN, 13));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }
    
    private JButton brandButton(String text, Color bg, Color fg) {
    	JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Poppins", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return b;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Manage Users":
            case "Manage Packages":
            case "View Analytics":
                JOptionPane.showMessageDialog(this, cmd + " — placeholder action");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Action: " + cmd);
        }
    }
    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            AdminDashboard ADb = new AdminDashboard("admin");
            ADb.setOpacity(0f);
            ADb.setVisible(true);
        });
    }
}