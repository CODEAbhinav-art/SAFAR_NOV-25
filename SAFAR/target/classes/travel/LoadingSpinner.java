package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D; 
import java.io.InputStream; 
import java.util.ArrayList; 
import java.util.List;     
import java.util.Random;   

public class LoadingSpinner extends JFrame implements Runnable {
    
    // Core Components
    private Thread loaderThread;
    private AnimatedSpinner spinner; 
    private JLabel loadingLabel;
    private JPanel mainPanel;
    private JPanel centerPanel; 
    private JPanel bottomPanel;

    // Animation Timers
    private Timer gameLoopTimer; // The ONE timer for everything  

    // State Machine
    private enum State { INTRO, LOADING, FADEOUT }
    private State currentState = State.INTRO;
    
    // Animation State
    private float animationProgress = 0f; 
    private float fadeOutProgress = 1.0f; 
    private double zoom = 1.0;            
    private double xPan = 0.0;            
    private volatile String currentLoadingMessage = "Launching Travel Planner...";
    
    // Particle List
    private List<Particle> particles = new ArrayList<>();
    
    // Ken Burns Panel
    private KenBurnsPanel imagePanel; 
    
    // Watermark
    private Image gsvLogoWatermark; 

    public LoadingSpinner() {
    	// Window setup
        setUndecorated(true);

        // ✨ --- 1. SETTING 800x500 SIZE ---
        int width = 800;
        int height = 500;
        setSize(width, height); 
        setLocationRelativeTo(null); // Center the window
        
        setBackground(new Color(0, 0, 0, 0)); 
        setResizable(false);
        setShape(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, 20, 20));

        // Initialize Particles
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            particles.add(new Particle(rand, width, height));
        }

        // Load the transparent logo image
        try {
            // ✨ --- PATH FIX: Removed the leading '/' ---
            ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("travel/icons/GSV_LOGO_Final.jpg"));
            gsvLogoWatermark = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error: Watermark logo not found!");
            gsvLogoWatermark = null;
        }


        // Gradient, Particle, AND Watermark Background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeOutProgress));
                
                // A. Draw Gradient
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(33, 147, 176),
                        getWidth(), getHeight(), new Color(109, 213, 237)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // B. Draw Particles
                for (Particle p : particles) {
                    p.draw(g2d);
                }

                // C. Draw the repeating transparent logos (watermark)
                if (gsvLogoWatermark != null) { // Check if logo loaded
                    int logoWidth = gsvLogoWatermark.getWidth(null);
                    int logoHeight = gsvLogoWatermark.getHeight(null);

                    if (logoWidth > 0 && logoHeight > 0) {
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f * fadeOutProgress)); // Fade watermark too

                        int numCols = (getWidth() / logoWidth) + 1;
                        int numRows = (getHeight() / logoHeight) + 1;
                        int startX = (getWidth() - (numCols * logoWidth)) / 2;
                        int startY = (getHeight() - (numRows * logoHeight)) / 2;

                        for (int row = 0; row < numRows; row++) {
                            for (int col = 0; col < numCols; col++) {
                                int x = startX + col * logoWidth;
                                int y = startY + row * logoHeight;
                                g2d.drawImage(gsvLogoWatermark, x, y, this);
                            }
                        }
                        
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeOutProgress));
                    }
                }
            }
        };
        
        mainPanel.setLayout(null); // Using null layout for "fly-in"
        setContentPane(mainPanel);

        // Load Fonts
        // ✨ --- PATH FIX: Removed the leading '/' ---
        Font poppinsBold = loadFont("travel/fonts/Poppins-Bold.ttf", 36f);
        Font poppinsRegular = loadFont("travel/fonts/Poppins-Regular.ttf", 14f);
        
        // Logo image for Ken Burns effect
        // ✨ --- PATH FIX: Removed the leading '/' ---
        ImageIcon palaceIcon = new ImageIcon(ClassLoader.getSystemResource("travel/icons/GSV_PALACE_F.jpg"));
        
        imagePanel = new KenBurnsPanel(palaceIcon.getImage());
        imagePanel.setOpaque(true); // Hides watermark behind it

        // ✨ --- 2. RECALCULATED SIZES FOR 800x500 ---
        int bottomPanelHeight = 180; 
        int centerPanelHeight = height - bottomPanelHeight; // 500 - 180 = 320

        // Center container (Image only)
        centerPanel = new JPanel(new BorderLayout()); 
        centerPanel.setOpaque(false); 
        centerPanel.add(imagePanel, BorderLayout.CENTER); 
        centerPanel.setSize(width, centerPanelHeight); // 800 x 320
        centerPanel.setLocation(0, -100); // Start off-screen
        mainPanel.add(centerPanel);

        // 🔄 Spinner
        spinner = new AnimatedSpinner(); 

        loadingLabel = new JLabel(currentLoadingMessage);
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(poppinsRegular); 
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Bottom Panel (Layout)
        bottomPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); 
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeOutProgress));
                g2d.setColor(new Color(0, 0, 0, 40)); 
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomPanel.setOpaque(false); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        JPanel loadingArea = new JPanel(new BorderLayout()); 
        loadingArea.setOpaque(false); 

        JPanel spinnerPanel = new JPanel();
        spinnerPanel.setOpaque(false);
        spinnerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); 
        spinnerPanel.add(spinner);
        
        JLabel themeLabel = new JLabel("TRAVEL PLANNER"); 
        themeLabel.setFont(poppinsBold.deriveFont(48f)); 
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        loadingArea.add(loadingLabel, BorderLayout.NORTH);  // Status
        loadingArea.add(spinnerPanel, BorderLayout.CENTER); // Spinner
        loadingArea.add(themeLabel, BorderLayout.SOUTH);    // Big Theme
        
        bottomPanel.add(loadingArea, BorderLayout.CENTER);

        // ✨ --- 3. RECALCULATED SIZES FOR 800x500 ---
        bottomPanel.setSize(width, bottomPanelHeight); // 800 x 180
        
        final int bottomPanel_Y_End = height - bottomPanelHeight; // 500 - 180 = 320
        
        bottomPanel.setLocation(0, height); // Start at y=500
        mainPanel.add(bottomPanel);
        
        
        // --- "GAME LOOP" TIMER ---
        gameLoopTimer = new Timer(30, new ActionListener() {
            // ✨ --- 4. RECALCULATED ANIMATION VALUES ---
            final int centerPanel_Y_End = 0; 
            final int bottomPanel_Y_End_Timer = bottomPanel_Y_End; // 320
            final int centerPanel_Y_Start = -100;
            final int bottomPanel_Y_Start = height; // 500

            public void actionPerformed(ActionEvent e) {
                
                switch (currentState) {
                    
                    case INTRO:
                        if (animationProgress < 1.0f) {
                            animationProgress += 0.03; 
                            if (animationProgress > 1.0f) animationProgress = 1.0f;
                            
                            double easedProgress = easeOutBack(animationProgress);

                            int center_y = (int) (centerPanel_Y_Start + ((centerPanel_Y_End - centerPanel_Y_Start) * easedProgress));
                            int bottom_y = (int) (bottomPanel_Y_Start + ((bottomPanel_Y_End_Timer - bottomPanel_Y_Start) * easedProgress));

                            centerPanel.setLocation(0, center_y);
                            bottomPanel.setLocation(0, bottom_y);
                            
                            setOpacity((float) animationProgress);
                            
                        } else {
                            centerPanel.setLocation(0, centerPanel_Y_End);
                            bottomPanel.setLocation(0, bottomPanel_Y_End_Timer);
                            setOpacity(1.0f);
                            currentState = State.LOADING; 
                        }
                        break;
                        
                    case LOADING:
                        zoom += 0.0001; 
                        xPan += 0.01;   
                        imagePanel.setZoom(zoom, xPan);
                        break;
                        
                    case FADEOUT:
                        if (fadeOutProgress > 0.0f) {
                            fadeOutProgress -= 0.05f; 
                            if (fadeOutProgress < 0.0f) fadeOutProgress = 0.0f;
                            setOpacity(fadeOutProgress);
                        } else {
                            gameLoopTimer.stop(); 
                            dispose(); 
                            new Login().setVisible(true); 
                        }
                        break;
                }

                if (currentState != State.FADEOUT) {
                    for (Particle p : particles) {
                        p.update(getWidth(), getHeight());
                    }
                    if (!loadingLabel.getText().equals(currentLoadingMessage)) {
                        loadingLabel.setText(currentLoadingMessage);
                    }
                    spinner.tick();
                    mainPanel.repaint();
                }
            }
        });
        gameLoopTimer.start(); 

        // Start loading simulation
        loaderThread = new Thread(this);
        loaderThread.start();
    }

    @Override
    public void run() {
        try {
        	String[] messages = {
                    "Connecting to servers...",
                    "Packing our bags...",
                    "Mapping your journey...",
                    "Checking for best routes...",
                    "Arriving at destination...",
                    "Ready to explore!"
                };

                for (String msg : messages) {
                    currentLoadingMessage = msg;
                    Thread.sleep(600); 
                }

            // Tell the game loop to take over the fade-out
            currentState = State.FADEOUT;
            
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
    
    // The "bouncy" math function
    private double easeOutBack(double x) {
        final double c1 = 1.70158;
        final double c3 = 1 + c1;
        return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
    }
    
    // Font Loading Function
    private Font loadFont(String path, float size) {
        try {
            // ✨ --- PATH FIX: Using this.getClass() and NO leading '/' ---
            InputStream is = this.getClass().getResourceAsStream(path);
            if (is == null) { 
                System.err.println("Error: Font not found at path: " + path);
                return new Font("Arial", Font.BOLD, (int) size);
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            return customFont;
        } catch (Exception e) {
            System.err.println("Could not load font: " + path);
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
    
    // AnimatedSpinner (Inner Class)
    private class AnimatedSpinner extends JComponent { 
        private int rotationAngle = 0;
        private final int arcAngle = 120;
        private final int strokeWidth = 8;
        public AnimatedSpinner() { setPreferredSize(new Dimension(80, 80)); setOpaque(false); }
        public void tick() { rotationAngle = (rotationAngle - 3) % 360; }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int diameter = Math.min(getWidth(), getHeight()) - (strokeWidth * 2);
            int x = (getWidth() - diameter) / 2; int y = (getHeight() - diameter) / 2;
            g2d.setColor(new Color(255, 255, 255, 200)); 
            g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Shape arc = new Arc2D.Float(x, y, diameter, diameter, rotationAngle, arcAngle, Arc2D.OPEN);
            g2d.draw(arc);
        }
    }
    
    // Ken Burns Panel (Inner Class)
    private class KenBurnsPanel extends JPanel {
        private Image image;
        private double scale = 1.0;
        private double xOffset = 0.0;
        public KenBurnsPanel(Image img) { this.image = img; setOpaque(true); }
        public void setZoom(double scale, double xOffset) { this.scale = scale; this.xOffset = xOffset; }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g; 
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int panelWidth = getWidth(); int panelHeight = getHeight();
            if (panelWidth == 0 || panelHeight == 0) return; 
            int imgWidth = image.getWidth(null); int imgHeight = image.getHeight(null);
            if (imgWidth <= 0 || imgHeight <= 0) return; 
            double drawRatio = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight) * this.scale;
            int newWidth = (int) (imgWidth * drawRatio); int newHeight = (int) (imgHeight * drawRatio);
            int x = (int) (this.xOffset - (newWidth - panelWidth) / 2);
            int y = (int) (-(newHeight - panelHeight) / 2);
            g2d.drawImage(image, x, y, newWidth, newHeight, this);
        }
    }

    // Particle (Inner Class)
    private class Particle {
        private float x, y;
        private float speed;
        private int alpha;
        private int size;
        private Random rand;
        public Particle(Random rand, int width, int height) { this.rand = rand; reset(width, height, true); }
        public void reset(int width, int height, boolean isNew) {
            this.x = rand.nextInt(width);
            this.y = isNew ? rand.nextInt(height) : height + rand.nextInt(100);
            this.speed = 1 + rand.nextFloat() * 2; 
            this.size = 2 + rand.nextInt(3); 
            this.alpha = 10 + rand.nextInt(40); 
        }
        public void update(int width, int height) { this.y -= speed; if (this.y < -size) { reset(width, height, false); } }
        public void draw(Graphics2D g2d) { g2d.setColor(new Color(255, 255, 255, alpha)); g2d.fillOval((int)x, (int)y, size, size); }
    }
}