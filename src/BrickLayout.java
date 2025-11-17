import java.util.List;

/**
 * Interface for generating brick layouts
 */
public interface BrickLayout {
    /**
     * Generates a list of bricks for the game
     * @param screenWidth Width of the game screen
     * @param screenHeight Height of the game screen
     * @param startY Starting Y position for bricks
     * @return List of Brick objects
     */
    List<Brick> generateBricks(int screenWidth, int screenHeight, int startY);
}

