package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WalletTopUpDialog extends JDialog implements ActionListener {

    private static final Color BRAND_BLUE = new Color(0x2451A6);
    private static final Color BRAND_TEAL = new Color(0x2C7A7B);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(34, 45, 65);
    private static final Font FONT_POPPINS_BOLD_16 = new Font("Poppins", Font.BOLD, 16);
    private static final Font FONT_POPPINS_PLAIN_14 = new Font("Poppins", Font.PLAIN, 14);

    private JTextField amountField;
    private JComboBox<String> paymentMethod;
    private JButton addButton, cancelButton;

    public WalletTopUpDialog(JFrame parent) {
        super(parent, "Add Money to Wallet", true);
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        JLabel title = new JLabel("Add Money to Your Wallet");
        title.setFont(new Font("Poppins", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);
        title.setBounds(70, 30, 300, 30);
        add(title);

        JLabel amountLabel = new JLabel("Enter Amount (₹):");
        amountLabel.setFont(FONT_POPPINS_PLAIN_14);
        amountLabel.setForeground(TEXT_DARK);
        amountLabel.setBounds(50, 90, 150, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(200, 90, 130, 30);
        amountField.setFont(FONT_POPPINS_PLAIN_14);
        amountField.setBackground(new Color(245, 247, 250));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 225)));
        add(amountField);

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(FONT_POPPINS_PLAIN_14);
        methodLabel.setForeground(TEXT_DARK);
        methodLabel.setBounds(50, 140, 150, 25);
        add(methodLabel);

        String[] methods = {"UPI", "Credit/Debit Card", "Net Banking"};
        paymentMethod = new JComboBox<>(methods);
        paymentMethod.setBounds(200, 140, 130, 30);
        paymentMethod.setFont(FONT_POPPINS_PLAIN_14);
        add(paymentMethod);

        addButton = new JButton("Add Money");
        addButton.setFont(FONT_POPPINS_BOLD_16);
        addButton.setBackground(BRAND_TEAL);
        addButton.setForeground(COLOR_WHITE);
        addButton.setFocusPainted(false);
        addButton.setBounds(70, 220, 120, 40);
        addButton.setBorder(BorderFactory.createEmptyBorder());
        addButton.addActionListener(this);
        add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(FONT_POPPINS_BOLD_16);
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(COLOR_WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBounds(210, 220, 120, 40);
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.addActionListener(this);
        add(cancelButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == addButton) {
            String amtText = amountField.getText().trim();
            if (amtText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double amount = Double.parseDouble(amtText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount greater than zero.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String method = (String) paymentMethod.getSelectedItem();
                JOptionPane.showMessageDialog(this,
                        "₹" + amount + " added successfully via " + method + "!",
                        "Wallet Updated",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Close dialog after success

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a numeric amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
