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
        setTitle("Neon Shatter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        
        pack();
        setLocationRelativeTo(null);
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

