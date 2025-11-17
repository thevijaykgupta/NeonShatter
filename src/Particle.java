import java.awt.*;

/**
 * Represents a particle effect when a brick is destroyed
 */
public class Particle {
    private double x, y;
    private double velocityX, velocityY;
    private int size;
    private Color color;
    private int life;
    private int maxLife;
    private boolean alive;
    
    public Particle(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = (int)(Math.random() * 4) + 2; // 2-5 pixels
        this.maxLife = (int)(Math.random() * 20) + 15; // 15-35 frames
        this.life = maxLife;
        this.alive = true;
        
        // Random velocity
        double angle = Math.random() * Math.PI * 2;
        double speed = Math.random() * 3 + 2; // 2-5 pixels per frame
        this.velocityX = Math.cos(angle) * speed;
        this.velocityY = Math.sin(angle) * speed;
    }
    
    public void update() {
        if (!alive) return;
        
        x += velocityX;
        y += velocityY;
        velocityY += 0.2; // Gravity effect
        
        life--;
        if (life <= 0) {
            alive = false;
        }
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void draw(Graphics2D g2d) {
        if (!alive) return;
        
        // Calculate alpha based on remaining life
        int alpha = (int)(255 * (life / (double)maxLife));
        alpha = Math.max(0, Math.min(255, alpha));
        
        Color particleColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        
        // Draw glow
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(particleColor.getRed(), particleColor.getGreen(), 
                              particleColor.getBlue(), alpha / 3));
        g2d.fillOval((int)(x - size), (int)(y - size), size * 3, size * 3);
        
        // Draw particle
        g2d.setColor(particleColor);
        g2d.fillOval((int)(x - size / 2), (int)(y - size / 2), size, size);
    }
}

