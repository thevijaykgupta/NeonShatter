import java.awt.*;

/**
 * Represents a single brick in the game
 */
public class Brick {
    private int x, y;
    private int width, height;
    private Color color;
    private boolean destroyed;
    private int flashTimer;
    
    public Brick(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.destroyed = false;
        this.flashTimer = 0;
    }
    
    public void flash() {
        flashTimer = 3; // 3 frames at 60fps = ~50ms
    }
    
    public void update() {
        if (flashTimer > 0) {
            flashTimer--;
        }
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
    
    private double twinklePhase = Math.random() * Math.PI * 2;
    
    public void draw(Graphics2D g2d) {
        if (destroyed) return;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Update twinkle effect
        twinklePhase += 0.15;
        double twinkleAlpha = 0.7 + 0.3 * Math.sin(twinklePhase);
        
        // Flash effect when hit
        Color drawColor = color;
        if (flashTimer > 0) {
            drawColor = new Color(255, 255, 255, 255); // Flash white
            twinkleAlpha = 1.0;
        }
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int starSize = Math.max(width, height) / 2;
        
        // Draw soft halo around star
        for (int i = 5; i > 0; i--) {
            int alpha = (int)((60 / i) * twinkleAlpha);
            int haloSize = starSize + i * 3;
            g2d.setColor(new Color(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue(), alpha));
            g2d.fillOval(centerX - haloSize, centerY - haloSize, haloSize * 2, haloSize * 2);
        }
        
        // Draw star sprite (4-pointed star)
        drawStar(g2d, centerX, centerY, starSize, drawColor, twinkleAlpha);
    }
    
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int size, Color color, double alpha) {
        int[] xPoints = new int[8];
        int[] yPoints = new int[8];
        
        // Create 4-pointed star
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            int radius = (i % 2 == 0) ? size : size / 2;
            xPoints[i] = centerX + (int)(radius * Math.cos(angle));
            yPoints[i] = centerY + (int)(radius * Math.sin(angle));
        }
        
        // Draw star with alpha
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
        g2d.fillPolygon(xPoints, yPoints, 8);
        
        // Draw bright center
        g2d.setColor(new Color(255, 255, 255, (int)(alpha * 200)));
        g2d.fillOval(centerX - size / 4, centerY - size / 4, size / 2, size / 2);
    }
}

