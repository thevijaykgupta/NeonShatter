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
    
    public void draw(Graphics2D g2d) {
        if (destroyed) return;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int cornerRadius = 8;
        
        // Flash effect when hit
        Color drawColor = color;
        if (flashTimer > 0) {
            drawColor = new Color(255, 255, 255, 255); // Flash white
        }
        
        // Draw soft blur shadow
        g2d.setColor(new Color(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue(), 40));
        g2d.fillRoundRect(x + 2, y + 2, width, height, cornerRadius, cornerRadius);
        
        // Draw outer glow effect (bright colored borders)
        for (int i = 4; i > 0; i--) {
            int alpha = Math.max(15, 70 / i);
            int offset = i * 2;
            g2d.setColor(new Color(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue(), alpha));
            g2d.fillRoundRect(x - offset, y - offset, 
                            width + offset * 2, height + offset * 2, 
                            cornerRadius + offset, cornerRadius + offset);
        }
        
        // Draw main brick
        g2d.setColor(drawColor);
        g2d.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        
        // Draw thin 1px bright highlight on top edge for depth
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.drawLine(x + cornerRadius, y, x + width - cornerRadius, y);
        g2d.drawLine(x, y + cornerRadius, x, y + height / 4);
        
        // Draw inner highlight for depth
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.fillRoundRect(x + 2, y + 2, width - 4, height / 3, cornerRadius, cornerRadius);
        
        // Draw bright neon border
        g2d.setColor(new Color(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue(), 255));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
    }
}

