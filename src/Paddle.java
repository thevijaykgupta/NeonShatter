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
    private boolean movingLeft;
    private boolean movingRight;
    
    public Paddle(int startX, int startY, int width, int height, int screenWidth) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.color = new Color(200, 200, 255, 255); // Spaceship color
        this.velocity = 0;
        this.movingLeft = false;
        this.movingRight = false;
    }
    
    public void moveLeft() {
        velocity -= ACCELERATION;
        if (velocity < -MAX_SPEED) {
            velocity = -MAX_SPEED;
        }
        movingLeft = true;
        movingRight = false;
    }
    
    public void moveRight() {
        velocity += ACCELERATION;
        if (velocity > MAX_SPEED) {
            velocity = MAX_SPEED;
        }
        movingRight = true;
        movingLeft = false;
    }
    
    public void stopMoving() {
        movingLeft = false;
        movingRight = false;
    }
    
    public void update() {
        // Apply friction
        velocity *= FRICTION;
        if (Math.abs(velocity) < 0.1) {
            velocity = 0;
            stopMoving();
        }
        
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
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        // Draw flame trail when moving
        if (movingLeft) {
            drawFlameTrail(g2d, x + width, centerY, 1);
        } else if (movingRight) {
            drawFlameTrail(g2d, x, centerY, -1);
        }
        
        // Draw spaceship body (simple triangle shape)
        int[] shipX = {centerX, x, x + width};
        int[] shipY = {y, y + height, y + height};
        g2d.setColor(new Color(200, 200, 255, 255));
        g2d.fillPolygon(shipX, shipY, 3);
        
        // Draw spaceship glow
        for (int i = 3; i > 0; i--) {
            int alpha = 40 / i;
            g2d.setColor(new Color(200, 200, 255, alpha));
            int[] glowX = {centerX, x - i, x + width + i};
            int[] glowY = {y - i, y + height + i, y + height + i};
            g2d.fillPolygon(glowX, glowY, 3);
        }
        
        // Draw cockpit window
        g2d.setColor(new Color(100, 150, 255, 180));
        g2d.fillOval(centerX - 8, y + 2, 16, 10);
        
        // Draw spaceship outline
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.drawPolygon(shipX, shipY, 3);
    }
    
    private void drawFlameTrail(Graphics2D g2d, int startX, int centerY, int direction) {
        int flameLength = 15;
        for (int i = 0; i < flameLength; i++) {
            double alpha = 1.0 - (double)i / flameLength;
            int flameX = startX + direction * i * 2;
            int flameWidth = (int)(8 * alpha);
            int flameHeight = (int)(6 * alpha);
            
            // Orange to yellow gradient
            Color flameColor = new Color(
                255, 
                (int)(200 + 55 * alpha), 
                (int)(100 * alpha), 
                (int)(alpha * 200)
            );
            g2d.setColor(flameColor);
            g2d.fillOval(flameX - flameWidth / 2, centerY - flameHeight / 2, flameWidth, flameHeight);
        }
    }
}

