import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip clip;
    private String soundFilePath;

    public SoundManager() {
        try {
            soundFilePath = "sounds/EatSound.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playEatSound() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }
}
