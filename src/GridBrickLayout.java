import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Classic grid layout for bricks
 */
public class GridBrickLayout implements BrickLayout {
    private static final Color[] COLORS = {
        new Color(0, 255, 255, 255),    // Cyan
        new Color(255, 0, 255, 255),    // Magenta
        new Color(0, 255, 0, 255),      // Green
        new Color(255, 255, 0, 255)     // Yellow
    };
    
    @Override
    public List<Brick> generateBricks(int screenWidth, int screenHeight, int startY) {
        List<Brick> bricks = new ArrayList<>();
        
        int rows = 5;
        int cols = 10;
        int brickWidth = 70;
        int brickHeight = 25;
        int spacing = 5;
        int totalWidth = cols * brickWidth + (cols - 1) * spacing;
        int startX = (screenWidth - totalWidth) / 2;
        
        for (int row = 0; row < rows; row++) {
            Color color = COLORS[row % COLORS.length];
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, color));
            }
        }
        
        return bricks;
    }
}

