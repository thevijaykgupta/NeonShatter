import javax.swing.*;
import java.awt.*;

/**
 * Neon Shatter - A modern take on the classic Brick Breaker game
 * 
 * @version 1.0
 */
public class NeonShatter extends JFrame {
    
    public NeonShatter() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Cosmic Starfall");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        
        // Remove insets to fill entire window
        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        
        // Ensure no gaps
        setUndecorated(false);
        Insets insets = getInsets();
        setSize(800 + insets.left + insets.right, 600 + insets.top + insets.bottom);
    }
    
    public static void main(String[] args) {
        // Run on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Ignore if L&F fails
                }
                
                NeonShatter game = new NeonShatter();
                game.setVisible(true);
                game.toFront();
                game.repaint();
            }
        });
    }
}

