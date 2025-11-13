package travel;
// check 1 in Loading Spinner
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingSpinner extends JFrame implements Runnable {
    private Thread loaderThread;
    private JProgressBar progressBar;
    private int progress = 0;
    private float opacityLevel = 0f;
    private Timer fadeTimer;
    private JPanel mainPanel;

    public LoadingSpinner() {
        // Window setup
        setUndecorated(true);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        // ✨ Gradient background like Eclipse splash
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(33, 147, 176), // top left teal
                        getWidth(), getHeight(), new Color(109, 213, 237) // bottom right light blue
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(mainPanel);

        // 🖼️ Logo image
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("travel/icons/GSV_Palace.jpg"));
        Image scaled = icon.getImage().getScaledInstance(700, 350, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 🧭 Title text
        JLabel titleLabel = new JLabel("Travel Planner");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Center container for logo + title
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(logoLabel, BorderLayout.CENTER);
        centerPanel.add(titleLabel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 🔄 Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(255, 255, 255));
        progressBar.setBackground(new Color(0, 0, 0, 50));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 22));
        progressBar.setFont(new Font("Poppins", Font.PLAIN, 13));

        JLabel loadingLabel = new JLabel("Launching Travel Planner...");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(loadingLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Fade-in animation
        fadeTimer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (opacityLevel < 1f) {
                    opacityLevel += 0.05f;
                    setOpacity(opacityLevel);
                } else {
                    fadeTimer.stop();
                }
            }
        });
        fadeTimer.start();

        // Start loading simulation
        loaderThread = new Thread(this);
        loaderThread.start();
    }

    @Override
    public void run() {
        try {
            while (progress <= 100) {
                progressBar.setValue(progress);
                Thread.sleep(45); // controls loading speed
                progress++;
            }

            // ✨ Fade-out before launching login
            for (float i = 1f; i >= 0f; i -= 0.05f) {
                setOpacity(i);
                Thread.sleep(30);
            }

            SwingUtilities.invokeLater(() -> {
                dispose();
                new ModeSelection().setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingSpinner splash = new LoadingSpinner();
            splash.setOpacity(0f);
            splash.setVisible(true);
        });
    }
}

