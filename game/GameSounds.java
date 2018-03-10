package game;

import javax.sound.sampled.*;
import java.io.File;

public class GameSounds {
    private Clip mClip;
    private File mSoundFile;
    private AudioInputStream mSound;

    public GameSounds(String source) {
        mSoundFile = new File(source);
        try {
            mSound = AudioSystem.getAudioInputStream(mSoundFile);
            mClip = (AudioSystem.getClip());
            mClip.open(mSound);
        } catch (Exception e) {
            // Swallow exception due to some computers not supporting little-endian audio format
        }
    }

    public void playSoundContinuously() {
        mClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSound() {
        mClip.start();
    }

    public void stopSound() {
        mClip.stop();
    }
}
