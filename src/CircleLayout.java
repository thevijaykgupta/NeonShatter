import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Circular/ring layout for bricks
 */
public class CircleLayout implements BrickLayout {
    private static final Color[] COLORS = {
        new Color(0, 255, 255, 255),    // Cyan
        new Color(255, 0, 255, 255),    // Magenta
        new Color(0, 255, 0, 255),      // Green
        new Color(255, 255, 0, 255)     // Yellow
    };
    
    @Override
    public List<Brick> generateBricks(int screenWidth, int screenHeight, int startY) {
        List<Brick> bricks = new ArrayList<>();
        
        int centerX = screenWidth / 2;
        int centerY = startY + 100;
        int brickWidth = 40;
        int brickHeight = 20;
        
        // Create 3 rings
        int[] ringRadii = {80, 120, 160};
        int[] bricksPerRing = {8, 12, 16};
        
        for (int ring = 0; ring < ringRadii.length; ring++) {
            int radius = ringRadii[ring];
            int count = bricksPerRing[ring];
            Color color = COLORS[ring % COLORS.length];
            
            for (int i = 0; i < count; i++) {
                double angle = (2 * Math.PI / count) * i;
                int x = (int)(centerX + Math.cos(angle) * radius - brickWidth / 2);
                int y = (int)(centerY + Math.sin(angle) * radius - brickHeight / 2);
                
                // Only add if within reasonable bounds
                if (x >= 0 && x + brickWidth <= screenWidth && 
                    y >= startY && y + brickHeight <= startY + 250) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, color));
                }
            }
        }
        
        return bricks;
    }
}

