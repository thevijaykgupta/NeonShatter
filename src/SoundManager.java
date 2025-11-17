import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Manages sound effects for the game
 */
public class SoundManager {
    private static boolean soundEnabled = true;
    
    public static void playSound(String soundName) {
        if (!soundEnabled) return;
        
        try {
            // Try to load sound file from resources
            String path = "sounds/" + soundName;
            File soundFile = new File(path);
            
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
        } catch (Exception e) {
            // Silently fail if sound file not found
        }
    }
    
    public static void playBrickHit() {
        playSound("zap.wav");
    }
    
    public static void playPaddleHit() {
        playSound("ping.wav");
    }
    
    public static void playBrickBreak() {
        playSound("crackle.wav");
    }
    
    public static void playGameOver() {
        playSound("gameover.wav");
    }
    
    public static void playLevelComplete() {
        playSound("levelup.wav");
    }
    
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }
}

