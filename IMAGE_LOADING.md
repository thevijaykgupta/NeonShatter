# Image Loading Instructions (Optional)

The game uses **procedural graphics** (drawn with code), so **no image files are required**. The game will work perfectly without any images.

However, if you want to use custom images instead:

## Optional Image Files

Place these in a `images/` folder:

- `spaceship.png` - Spaceship sprite (100x15 pixels recommended)
- `star_yellow.png` - Yellow star sprite
- `star_blue.png` - Blue star sprite  
- `star_white.png` - White star sprite
- `star_purple.png` - Purple star sprite
- `comet.png` - Comet/ball sprite (20x20 pixels recommended)
- `nebula.png` - Large nebula background (800x600 pixels)

## Code Changes Needed

To use images, modify the draw methods in:
- `Brick.java` - Replace `drawStar()` with image drawing
- `Paddle.java` - Replace spaceship polygon with image
- `Ball.java` - Replace comet circle with image
- `GamePanel.java` - Replace nebula layers with image

Example image loading code:
```java
BufferedImage image = null;
try {
    image = ImageIO.read(new File("images/spaceship.png"));
} catch (IOException e) {
    // Fallback to procedural graphics
}
```

The current implementation uses procedural graphics that look great without any image files!

