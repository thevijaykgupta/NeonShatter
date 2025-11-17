import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the ball in the game
 */
public class Ball {
    private double x, y;
    private double velocityX, velocityY;
    private int radius;
    private Color color;
    private List<Point> trail;
    private static final int MAX_TRAIL_LENGTH = 15;
    
    // Ball speed constants
    private static final double BASE_SPEED = 4.0;
    private static final double MAX_SPEED = 7.0;
    
    public Ball(int startX, int startY, int radius) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
        this.color = new Color(150, 200, 255, 255); // Comet blue-white
        this.trail = new ArrayList<>();
        reset();
    }
    
    public void reset() {
        // Start with random direction
        double angle = Math.random() * Math.PI / 3 + Math.PI / 6; // 30-90 degrees
        velocityX = BASE_SPEED * Math.cos(angle);
        velocityY = -BASE_SPEED * Math.sin(angle);
    }
    
    public void update() {
        // Add current position to trail
        trail.add(new Point((int)x, (int)y));
        if (trail.size() > MAX_TRAIL_LENGTH) {
            trail.remove(0);
        }
        
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
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw motion trail (fading tail)
        if (trail.size() > 1) {
            for (int i = 0; i < trail.size() - 1; i++) {
                Point p1 = trail.get(i);
                Point p2 = trail.get(i + 1);
                double alpha = (double)(i + 1) / trail.size() * 0.6;
                int trailSize = (int)(radius * 0.5 * alpha);
                g2d.setColor(new Color(150, 200, 255, (int)(alpha * 255)));
                g2d.fillOval(p1.x - trailSize, p1.y - trailSize, trailSize * 2, trailSize * 2);
            }
        }
        
        // Draw comet glow effect
        for (int i = 5; i > 0; i--) {
            int alpha = Math.max(10, 80 / i);
            int glowRadius = radius + i * 2;
            g2d.setColor(new Color(150, 200, 255, alpha));
            g2d.fillOval((int)(x - glowRadius), (int)(y - glowRadius), 
                        glowRadius * 2, glowRadius * 2);
        }
        
        // Draw main comet orb
        g2d.setColor(color);
        g2d.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        
        // Draw bright core
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval((int)(x - radius * 0.6), (int)(y - radius * 0.6), 
                    (int)(radius * 1.2), (int)(radius * 1.2));
    }
}

