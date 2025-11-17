import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Zigzag/diagonal pattern layout
 */
public class ZigZagLayout implements BrickLayout {
    private static final Color[] COLORS = {
        new Color(255, 255, 100, 255),  // Yellow star
        new Color(100, 150, 255, 255),  // Blue star
        new Color(255, 255, 255, 255),  // White star
        new Color(200, 100, 255, 255)   // Purple star
    };
    
    @Override
    public List<Brick> generateBricks(int screenWidth, int screenHeight, int startY) {
        List<Brick> bricks = new ArrayList<>();
        
        int rows = 7;
        int cols = 11;
        int brickWidth = 60;
        int brickHeight = 24;
        int spacing = 5;
        int zigzagOffset = 30; // How much to shift each row
        
        int totalWidth = cols * brickWidth + (cols - 1) * spacing;
        int baseStartX = (screenWidth - totalWidth) / 2;
        
        for (int row = 0; row < rows; row++) {
            Color color = COLORS[row % COLORS.length];
            // Alternate direction for zigzag effect
            int offset = (row % 2 == 0) ? 0 : zigzagOffset;
            int startX = baseStartX + offset;
            
            // Adjust column count for offset rows
            int actualCols = (row % 2 == 0) ? cols : cols - 1;
            
            for (int col = 0; col < actualCols; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);
                
                if (x >= 0 && x + brickWidth <= screenWidth) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, color));
                }
            }
        }
        
        return bricks;
    }
}

