import java.awt.*;

/**
 * Represents the ball in the game
 */
public class Ball {
    private double x, y;
    private double velocityX, velocityY;
    private int radius;
    private Color color;
    
    // Ball speed constants
    private static final double BASE_SPEED = 4.0;
    private static final double MAX_SPEED = 7.0;
    
    public Ball(int startX, int startY, int radius) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
        this.color = new Color(0, 255, 255, 255); // Cyan
        reset();
    }
    
    public void reset() {
        // Start with random direction
        double angle = Math.random() * Math.PI / 3 + Math.PI / 6; // 30-90 degrees
        velocityX = BASE_SPEED * Math.cos(angle);
        velocityY = -BASE_SPEED * Math.sin(angle);
    }
    
    public void update() {
        x += velocityX;
        y += velocityY;
    }
    
    public void reverseX() {
        velocityX = -velocityX;
    }
    
    public void reverseY() {
        velocityY = -velocityY;
    }
    
    public void setVelocity(double vx, double vy) {
        // Normalize to maintain consistent speed
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > MAX_SPEED) {
            vx = (vx / speed) * MAX_SPEED;
            vy = (vy / speed) * MAX_SPEED;
        }
        velocityX = vx;
        velocityY = vy;
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getRadius() {
        return radius;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }
    
    public double getVelocityX() {
        return velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }
    
    public void draw(Graphics2D g2d) {
        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw glow effect (outer layers)
        for (int i = 3; i > 0; i--) {
            int alpha = 30 / i;
            int glowRadius = radius + i * 2;
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g2d.fillOval((int)(x - glowRadius), (int)(y - glowRadius), 
                        glowRadius * 2, glowRadius * 2);
        }
        
        // Draw main ball
        g2d.setColor(color);
        g2d.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        
        // Draw inner highlight
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval((int)(x - radius / 2), (int)(y - radius / 2), radius, radius);
    }
}

