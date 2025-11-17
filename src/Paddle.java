import java.awt.*;

/**
 * Represents the player's paddle
 */
public class Paddle {
    private int x, y;
    private int width, height;
    private int screenWidth;
    private Color color;
    
    // Movement properties
    private double velocity;
    private static final double ACCELERATION = 0.5;
    private static final double FRICTION = 0.85;
    private static final double MAX_SPEED = 8.0;
    
    public Paddle(int startX, int startY, int width, int height, int screenWidth) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.color = new Color(255, 0, 255, 255); // Magenta/Pink
        this.velocity = 0;
    }
    
    public void moveLeft() {
        velocity -= ACCELERATION;
        if (velocity < -MAX_SPEED) {
            velocity = -MAX_SPEED;
        }
    }
    
    public void moveRight() {
        velocity += ACCELERATION;
        if (velocity > MAX_SPEED) {
            velocity = MAX_SPEED;
        }
    }
    
    public void update() {
        // Apply friction
        velocity *= FRICTION;
        
        // Update position
        x += (int)velocity;
        
        // Keep paddle within screen bounds
        if (x < 0) {
            x = 0;
            velocity = 0;
        } else if (x + width > screenWidth) {
            x = screenWidth - width;
            velocity = 0;
        }
    }
    
    public void reset(int startX) {
        this.x = startX;
        this.velocity = 0;
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
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void draw(Graphics2D g2d) {
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cornerRadius = 8;
        
        // Draw glow effect
        for (int i = 4; i > 0; i--) {
            int alpha = 40 / i;
            int offset = i * 2;
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g2d.fillRoundRect(x - offset, y - offset, 
                            width + offset * 2, height + offset * 2, 
                            cornerRadius + offset, cornerRadius + offset);
        }
        
        // Draw main paddle
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        
        // Draw inner highlight
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.fillRoundRect(x + 2, y + 2, width - 4, height / 2, cornerRadius, cornerRadius);
    }
}

