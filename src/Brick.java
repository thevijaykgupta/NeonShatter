import java.awt.*;

/**
 * Represents a single brick in the game
 */
public class Brick {
    private int x, y;
    private int width, height;
    private Color color;
    private boolean destroyed;
    
    public Brick(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.destroyed = false;
    }
    
    public void destroy() {
        destroyed = true;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void draw(Graphics2D g2d) {
        if (destroyed) return;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cornerRadius = 6;
        
        // Draw glow effect (outer layers)
        for (int i = 3; i > 0; i--) {
            int alpha = 50 / i;
            int offset = i * 2;
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g2d.fillRoundRect(x - offset, y - offset, 
                            width + offset * 2, height + offset * 2, 
                            cornerRadius + offset, cornerRadius + offset);
        }
        
        // Draw main brick
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        
        // Draw inner highlight for depth
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillRoundRect(x + 2, y + 2, width - 4, height / 3, cornerRadius, cornerRadius);
        
        // Draw border
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }
}

