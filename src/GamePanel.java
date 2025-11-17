import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(10, 10, 20)); // Dark background
        setFocusable(true);
        addKeyListener(this);
        
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
                
                // Create particles
                createParticles(brick.getX() + brick.getWidth() / 2, 
                              brick.getY() + brick.getHeight() / 2, 
                              brick.getColor());
                
                // Destroy brick
                brick.destroy();
                score += 10;
                
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
            }
        }
        
        // Check if ball fell off screen
        if (ball.getY() - ball.getRadius() > HEIGHT) {
            gameState = GameState.GAME_OVER;
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
        
        // Draw game elements
        if (gameState == GameState.PLAYING || gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            // Draw bricks
            for (Brick brick : bricks) {
                brick.draw(g2d);
            }
            
            // Draw particles
            for (Particle particle : particles) {
                particle.draw(g2d);
            }
            
            // Draw paddle
            paddle.draw(g2d);
            
            // Draw ball
            ball.draw(g2d);
        }
        
        // Draw UI
        drawUI(g2d);
    }
    
    private void drawUI(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(new Color(255, 255, 255, 255));
        
        // Draw score
        String scoreText = "Score: " + score;
        g2d.drawString(scoreText, 20, 30);
        
        // Draw game state messages
        if (gameState == GameState.MENU) {
            drawCenteredText(g2d, "NEON SHATTER", 40, new Color(0, 255, 255, 255));
            drawCenteredText(g2d, "Press ENTER or SPACE to Start", 80, new Color(255, 255, 255, 200));
            drawCenteredText(g2d, "Use Arrow Keys to Move", 110, new Color(255, 255, 255, 150));
        } else if (gameState == GameState.GAME_OVER) {
            drawCenteredText(g2d, "GAME OVER", 40, new Color(255, 0, 0, 255));
            drawCenteredText(g2d, "Final Score: " + score, 80, new Color(255, 255, 255, 200));
            drawCenteredText(g2d, "Press ENTER to Restart", 110, new Color(255, 255, 255, 150));
        } else if (gameState == GameState.WIN) {
            drawCenteredText(g2d, "YOU WIN!", 40, new Color(0, 255, 0, 255));
            drawCenteredText(g2d, "Final Score: " + score, 80, new Color(255, 255, 255, 200));
            drawCenteredText(g2d, "Press ENTER to Play Again", 110, new Color(255, 255, 255, 150));
        }
    }
    
    private void drawCenteredText(Graphics2D g2d, String text, int y, Color color) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        g2d.setColor(color);
        g2d.drawString(text, x, y);
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

