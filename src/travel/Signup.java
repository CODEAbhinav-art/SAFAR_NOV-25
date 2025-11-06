package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Signup extends JFrame implements ActionListener{
	
	private JTextField usernameField, fullnameField, answerField, emailField;
	private JPasswordField passwordField;
	private Choice securityChoice;
	private JButton createButton, backButton;
	
	public Signup() {
		setTitle("Travel Planner - Sign Up");
		setSize(1100, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	
		//Main Panel
		JPanel mainPan = new JPanel(new BorderLayout());
		add(mainPan);
		
		//leftpanel
		JPanel leftPan = new JPanel(new BorderLayout());
		leftPan.setPreferredSize(new Dimension(320,550));
		leftPan.setBackground(new Color(0x2452A6));
		JLabel logo = new JLabel("Join SAFAR ✈", SwingConstants.CENTER);
		logo.setFont(new Font("Poppins", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        leftPan.add(logo, BorderLayout.CENTER);
        mainPan.add(leftPan, BorderLayout.WEST);
        
        //right panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        mainPan.add(formPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(new Color(34, 45, 65));
        title.setBounds(60, 40, 400, 40);
        formPanel.add(title);
        
        JLabel subtitle = new JLabel("Sign up to explore the world with SAFAR");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setBounds(60, 80, 350, 25);
        formPanel.add(subtitle);
        
        addLabel("Username", 60, 130, formPanel);
        usernameField = addTextField(60, 155, 320, 40, formPanel);
        
        addLabel("Full Name", 60, 205, formPanel);
        usernameField = addTextField(60, 230, 320, 40, formPanel);
        
        addLabel("Password", 60, 280, formPanel);
        passwordField = addPasswordField(60, 305, 320, 40, formPanel);
        
            // Email
        addLabel("Email", 420, 155, formPanel);
        emailField = addTextField(420, 180, 320, 40, formPanel);
        
     // ==== Buttons ====
        createButton = createButton("Create Account", new Color(0x2451A6), Color.WHITE, 420, 260, 200, 45, formPanel);
        backButton = createButton("Back to Login", new Color(0x2C7A7B), Color.WHITE, 420, 320, 200, 45, formPanel);
        
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
		tf.setFont(new Font("Poppins",Font.PLAIN, 14));
		tf.setBackground(new Color(240,240,240));
		tf.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
		panel.add(tf);
		return tf;
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
	
    private JPasswordField addPasswordField(int x, int y, int w, int h, JPanel panel) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y, w, h);
        pf.setFont(new Font("Poppins", Font.PLAIN, 14));
        pf.setBackground(new Color(240, 240, 240));
        pf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(pf);
        return pf;
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == backButton) {
    		new Login().setVisible(true);
    		this.dispose();
    	}else if(e.getSource() == createButton) {
    		JOptionPane.showMessageDialog(this, "SQL INTEGRATION IS YET TO BE DONE..PLEASE WAIT TILL THEN");
            new Login().setVisible(true);
            this.dispose();
    	}
    	
    }
	
	public static void main(String[] args) {
		new Signup().setVisible(true);;
	}
}