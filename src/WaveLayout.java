import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Wave layout with a sinusoidal pattern
 */
public class WaveLayout implements BrickLayout {
    private static final Color[] COLORS = {
        new Color(0, 255, 255, 255),    // Cyan
        new Color(255, 0, 255, 255),    // Magenta
        new Color(0, 255, 0, 255),      // Green
        new Color(255, 255, 0, 255)     // Yellow
    };
    
    @Override
    public List<Brick> generateBricks(int screenWidth, int screenHeight, int startY) {
        List<Brick> bricks = new ArrayList<>();
        
        int rows = 6;
        int cols = 12;
        int brickWidth = 55;
        int brickHeight = 22;
        int spacing = 4;
        double waveAmplitude = 15.0;
        double waveFrequency = 0.3;
        
        for (int row = 0; row < rows; row++) {
            Color color = COLORS[row % COLORS.length];
            double phase = row * 0.5; // Offset each row
            
            for (int col = 0; col < cols; col++) {
                // Calculate wave offset
                double waveOffset = Math.sin(col * waveFrequency + phase) * waveAmplitude;
                int baseX = (screenWidth - (cols * (brickWidth + spacing))) / 2;
                int x = baseX + col * (brickWidth + spacing) + (int)waveOffset;
                int y = startY + row * (brickHeight + spacing);
                
                // Make sure bricks stay within screen bounds
                if (x >= 0 && x + brickWidth <= screenWidth) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, color));
                }
            }
        }
        
        return bricks;
    }
}

