import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private final Clip eatSoundClip;
    private final Clip backgroundMusicClip;

    public SoundManager() {
        this.eatSoundClip = loadClip("sounds/EatSound.wav");
        this.backgroundMusicClip = loadClip("sounds/SnakeMusic.wav");

        if (backgroundMusicClip != null) {
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private Clip loadClip(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playEatSound() {
        if (eatSoundClip != null) {
            eatSoundClip.setFramePosition(0); // Rewind to the beginning
            eatSoundClip.start();
        }
    }
}
