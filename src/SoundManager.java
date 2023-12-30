import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip backgroundMusicClip;
    private Clip eatSoundClip;
    private Clip gameOverSoundClip;

    public SoundManager() {
        initialize();
    }

    private void initialize() {
        // Load background music
        backgroundMusicClip = loadClip("sounds/SnakeMusic.wav");

        // Load eat sound
        eatSoundClip = loadClip("sounds/EatSound.wav");

        gameOverSoundClip = loadClip("sounds/GameOver.wav");
    }

    private Clip loadClip(String path) {
        try {
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void playBackgroundMusic() {
        if (backgroundMusicClip != null && !backgroundMusicClip.isRunning()) {
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-5f); // Adjust volume (default is -80f, lower values are louder)

            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            playGameOverSound();
        }
    }

    public void playEatSound() {
        if (eatSoundClip != null && !eatSoundClip.isRunning()) {
            eatSoundClip.setFramePosition(0); // Rewind to the beginning
            eatSoundClip.start();
        }
    }

    public void playGameOverSound(){
        gameOverSoundClip.setFramePosition(0);
        gameOverSoundClip.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        playBackgroundMusic();
    }
}
