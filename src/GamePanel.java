import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Star particle for parallax background
 */
class StarParticle {
    private double x, y;
    private double speed;
    private int size;
    private double twinklePhase;
    
    public StarParticle() {
        reset();
    }
    
    public void reset() {
        x = Math.random() * 800;
        y = Math.random() * 600;
        speed = Math.random() * 0.3 + 0.05;
        size = (int)(Math.random() * 2) + 1;
        twinklePhase = Math.random() * Math.PI * 2;
    }
    
    public void update() {
        y += speed;
        twinklePhase += 0.1;
        if (y > 600) {
            reset();
            y = -5;
        }
    }
    
    public void draw(Graphics2D g2d) {
        int alpha = (int)(100 + 50 * Math.sin(twinklePhase));
        alpha = Math.max(50, Math.min(150, alpha));
        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.fillOval((int)x, (int)y, size, size);
    }
}

/**
 * Nebula layer for parallax background
 */
class NebulaLayer {
    private double offset;
    private double speed;
    private Color[] nebulaColors;
    
    public NebulaLayer(double speed) {
        this.speed = speed;
        this.offset = 0;
        this.nebulaColors = new Color[] {
            new Color(100, 50, 150, 20),  // Purple
            new Color(50, 100, 200, 15),  // Blue
            new Color(150, 50, 100, 18)   // Magenta
        };
    }
    
    public void update() {
        offset += speed;
        if (offset > 800) offset = 0;
    }
    
    public void draw(Graphics2D g2d, int width, int height) {
        for (int i = 0; i < nebulaColors.length; i++) {
            g2d.setColor(nebulaColors[i]);
            int x = (int)(offset + i * 200) % (width + 400) - 200;
            g2d.fillOval(x, height / 4, 400, 300);
            g2d.fillOval(x + 300, height / 2, 350, 250);
        }
    }
}

/**
 * Main game panel that handles rendering, game logic, and input
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener {
    
    // Game dimensions
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    // Game entities
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<Particle> particles;
    
    // Game state
    private GameState gameState;
    private int score;
    private int currentLayoutIndex;
    private BrickLayout[] layouts;
    
    // Timing
    private Timer gameTimer;
    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;
    
    // Input handling
    private boolean leftPressed, rightPressed;
    
    // Background effects
    private List<StarParticle> starParticles;
    private NebulaLayer nebulaLayer1;
    private NebulaLayer nebulaLayer2;
    private int frameCount;
    
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(5, 5, 20)); // Deep space
        setFocusable(true);
        addKeyListener(this);
        setOpaque(true);
        
        // Initialize background effects
        starParticles = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            starParticles.add(new StarParticle());
        }
        nebulaLayer1 = new NebulaLayer(0.1);
        nebulaLayer2 = new NebulaLayer(0.05);
        frameCount = 0;
        
        // Initialize layouts
        layouts = new BrickLayout[] {
            new GridBrickLayout(),
            new WaveLayout(),
            new CircleLayout(),
            new ZigZagLayout()
        };
        
        initializeGame();
        
        // Start game loop
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }
    
    private void initializeGame() {
        // Initialize paddle
        int paddleWidth = 100;
        int paddleHeight = 15;
        int paddleX = (WIDTH - paddleWidth) / 2;
        int paddleY = HEIGHT - 50;
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, WIDTH);
        
        // Initialize ball
        int ballRadius = 10;
        int ballX = WIDTH / 2;
        int ballY = HEIGHT - 80;
        ball = new Ball(ballX, ballY, ballRadius);
        
        // Initialize bricks
        currentLayoutIndex = 0;
        loadBrickLayout();
        
        // Initialize particles
        particles = new ArrayList<>();
        
        // Reset game state
        gameState = GameState.MENU;
        score = 0;
    }
    
    private void loadBrickLayout() {
        bricks = layouts[currentLayoutIndex].generateBricks(WIDTH, HEIGHT, 50);
    }
    
    private void startGame() {
        if (gameState == GameState.MENU || gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            initializeGame();
            gameState = GameState.PLAYING;
            ball.reset();
        }
    }
    
    private void updateGame() {
        if (gameState != GameState.PLAYING) return;
        
        // Update paddle
        if (leftPressed) paddle.moveLeft();
        if (rightPressed) paddle.moveRight();
        paddle.update();
        
        // Update ball
        ball.update();
        
        // Check ball collision with walls
        if (ball.getX() - ball.getRadius() <= 0 || 
            ball.getX() + ball.getRadius() >= WIDTH) {
            ball.reverseX();
        }
        if (ball.getY() - ball.getRadius() <= 0) {
            ball.reverseY();
        }
        
        // Check ball collision with paddle
        Rectangle ballBounds = ball.getBounds();
        Rectangle paddleBounds = paddle.getBounds();
        if (ballBounds.intersects(paddleBounds)) {
            // Calculate hit position on paddle (-1 to 1, where 0 is center)
            double hitPos = (ball.getX() - (paddle.getX() + paddle.getWidth() / 2.0)) / (paddle.getWidth() / 2.0);
            hitPos = Math.max(-1, Math.min(1, hitPos));
            
            // Adjust ball angle based on hit position
            double angle = Math.PI / 2 + hitPos * Math.PI / 3; // -60 to +60 degrees from vertical
            double currentVx = ball.getVelocityX();
            double currentVy = ball.getVelocityY();
            double speed = Math.sqrt(currentVx * currentVx + currentVy * currentVy);
            ball.setVelocity(Math.sin(angle) * speed, -Math.cos(angle) * speed);
            
            // Make sure ball goes up
            if (ball.getVelocityY() > 0) {
                ball.reverseY();
            }
            
            // Play paddle hit sound
            SoundManager.playPaddleHit();
        }
        
        // Check ball collision with bricks
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (!brick.isDestroyed() && ballBounds.intersects(brick.getBounds())) {
                // Determine collision side
                double ballCenterX = ball.getX();
                double ballCenterY = ball.getY();
                double brickCenterX = brick.getX() + brick.getWidth() / 2.0;
                double brickCenterY = brick.getY() + brick.getHeight() / 2.0;
                
                double dx = ballCenterX - brickCenterX;
                double dy = ballCenterY - brickCenterY;
                
                // Flash brick and play sound
                brick.flash();
                SoundManager.playBrickHit();
                
                // Create particles
                createParticles(brick.getX() + brick.getWidth() / 2, 
                              brick.getY() + brick.getHeight() / 2, 
                              brick.getColor());
                
                // Destroy brick
                brick.destroy();
                score += 10;
                
                // Play break sound
                SoundManager.playBrickBreak();
                
                // Bounce ball based on collision side
                if (Math.abs(dx) > Math.abs(dy)) {
                    ball.reverseX();
                } else {
                    ball.reverseY();
                }
                
                break;
            }
        }
        
        // Check if all bricks are destroyed
        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        
        if (allDestroyed) {
            // Move to next layout or win
            currentLayoutIndex++;
            if (currentLayoutIndex >= layouts.length) {
                gameState = GameState.WIN;
            } else {
                loadBrickLayout();
                // Reset ball position
                ball.setPosition(WIDTH / 2, HEIGHT - 80);
                ball.reset();
                SoundManager.playLevelComplete();
            }
        }
        
        // Check if ball fell off screen
        if (ball.getY() - ball.getRadius() > HEIGHT) {
            gameState = GameState.GAME_OVER;
            SoundManager.playGameOver();
        }
        
        // Update bricks (for flash effect)
        for (Brick brick : bricks) {
            brick.update();
        }
        
        // Update background effects
        frameCount++;
        nebulaLayer1.update();
        nebulaLayer2.update();
        for (StarParticle p : starParticles) {
            p.update();
        }
        
        // Update particles
        Iterator<Particle> particleIterator = particles.iterator();
        while (particleIterator.hasNext()) {
            Particle particle = particleIterator.next();
            particle.update();
            if (!particle.isAlive()) {
                particleIterator.remove();
            }
        }
    }
    
    private void createParticles(int x, int y, Color color) {
        int count = (int)(Math.random() * 8) + 5; // 5-12 particles
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(x, y, color));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable better rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        
        // Draw deep space background
        g2d.setColor(new Color(5, 5, 20));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Draw parallax nebula layers
        nebulaLayer2.draw(g2d, WIDTH, HEIGHT);
        nebulaLayer1.draw(g2d, WIDTH, HEIGHT);
        
        // Draw star particles
        for (StarParticle p : starParticles) {
            p.draw(g2d);
        }
        
        // Draw game elements
        if (gameState == GameState.PLAYING || gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            // Draw bricks (stars)
            for (Brick brick : bricks) {
                brick.draw(g2d);
            }
            
            // Draw particles
            for (Particle particle : particles) {
                particle.draw(g2d);
            }
            
            // Draw paddle (spaceship)
            paddle.draw(g2d);
            
            // Draw ball (comet)
            ball.draw(g2d);
        }
        
        // Draw UI
        drawUI(g2d);
    }
    
    private void drawUI(Graphics2D g2d) {
        // Score font - Orbitron / Segoe UI fallback
        Font scoreFont = new Font("Segoe UI", Font.BOLD, 24);
        g2d.setFont(scoreFont);
        
        // Draw score with cyan/white glow
        String scoreText = "Score: " + score;
        drawTextWithGlow(g2d, scoreText, 20, 30, new Color(0, 255, 255, 255));
        
        // Draw game state messages
        if (gameState == GameState.MENU) {
            // Title font - Orbitron / Segoe UI fallback
            Font titleFont = new Font("Segoe UI", Font.BOLD, 56);
            g2d.setFont(titleFont);
            drawCenteredText(g2d, "COSMIC STARFALL", 80, new Color(0, 255, 255, 255));
            
            // Button font - Segoe UI
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);
            g2d.setFont(buttonFont);
            drawCenteredText(g2d, "Press ENTER or SPACE to Start", 140, new Color(255, 255, 255, 220));
            drawCenteredText(g2d, "Use Arrow Keys to Move", 170, new Color(255, 255, 255, 180));
        } else if (gameState == GameState.GAME_OVER) {
            Font titleFont = new Font("Segoe UI", Font.BOLD, 56);
            g2d.setFont(titleFont);
            drawCenteredText(g2d, "GAME OVER", 80, new Color(255, 100, 100, 255));
            
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);
            g2d.setFont(buttonFont);
            drawCenteredText(g2d, "Final Score: " + score, 140, new Color(255, 255, 255, 220));
            drawCenteredText(g2d, "Press ENTER to Restart", 170, new Color(255, 255, 255, 180));
        } else if (gameState == GameState.WIN) {
            Font titleFont = new Font("Segoe UI", Font.BOLD, 56);
            g2d.setFont(titleFont);
            drawCenteredText(g2d, "VICTORY!", 80, new Color(255, 255, 100, 255));
            
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);
            g2d.setFont(buttonFont);
            drawCenteredText(g2d, "Final Score: " + score, 140, new Color(255, 255, 255, 220));
            drawCenteredText(g2d, "Press ENTER to Play Again", 170, new Color(255, 255, 255, 180));
        }
    }
    
    private void drawTextWithGlow(Graphics2D g2d, String text, int x, int y, Color color) {
        FontMetrics fm = g2d.getFontMetrics();
        // Draw glow layers
        for (int i = 3; i > 0; i--) {
            int alpha = 60 / i;
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g2d.drawString(text, x + i, y + i);
            g2d.drawString(text, x - i, y - i);
            g2d.drawString(text, x + i, y - i);
            g2d.drawString(text, x - i, y + i);
        }
        // Draw main text
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    private void drawCenteredText(Graphics2D g2d, String text, int y, Color color) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        drawTextWithGlow(g2d, text, x, y, color);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
            startGame();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}

