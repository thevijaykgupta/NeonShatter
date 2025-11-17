import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Classic grid layout for bricks
 */
public class GridBrickLayout implements BrickLayout {
    private static final Color[] COLORS = {
        new Color(255, 255, 100, 255),  // Yellow star
        new Color(100, 150, 255, 255),  // Blue star
        new Color(255, 255, 255, 255),  // White star
        new Color(200, 100, 255, 255)   // Purple star
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

