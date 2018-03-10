package koalaGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Exit extends KoalaGameObject {

    private final static String SAVED_URI = "resources/koala_saved.wav";
    private KoalaGameWorld mContext;

    public Exit(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
    }

    public void update() {
        if (mContext.getKoalaOne().collision(getX(), getY(), getWidth(), getHeight())) {
            GameSounds mSavedSound = new GameSounds(SAVED_URI);
            mSavedSound.playSound();
            mContext.getKoalaOne().remove();
        }
        if (mContext.getKoalaTwo().collision(getX(), getY(), getWidth(), getHeight())) {
            GameSounds mSavedSound = new GameSounds(SAVED_URI);
            mSavedSound.playSound();
            mContext.getKoalaTwo().remove();
        }
        if (mContext.getKoalaThree().collision(getX(), getY(), getWidth(), getHeight())) {
            GameSounds mSavedSound = new GameSounds(SAVED_URI);
            mSavedSound.playSound();
            mContext.getKoalaThree().remove();
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        update();
        g.drawImage(getImage(), mXPosition, mYPosition, obs);
    }
}