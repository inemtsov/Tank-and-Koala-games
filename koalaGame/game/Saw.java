package koalaGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Saw extends KoalaGameObject {

    private GameSounds mSawSound;
    private final static String SAW_SOUND_URI = "resources/koala_saw.wav";
    private KoalaGameWorld mContext;
    private static final int SAW_BOUNDS_OFFSET = 10;
    private int mSawInitialPosition;
    private int mLowerMoveBound;
    private boolean mSawMoveUp;

    public Saw(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
        mSawSound = new GameSounds(SAW_SOUND_URI);
        mSawInitialPosition  = yPosition;
        mLowerMoveBound = yPosition + 120;
    }

    public void update() {
        if (mContext.getKoalaOne().collision(getX(), getY(), getWidth() - SAW_BOUNDS_OFFSET, getHeight() - SAW_BOUNDS_OFFSET)) {
            mSawSound.playSound();
            mContext.getKoalaOne().die();
            mContext.setGameToFinish();
        }
        if (mContext.getKoalaTwo().collision(getX(), getY(), getWidth() - SAW_BOUNDS_OFFSET, getHeight() - SAW_BOUNDS_OFFSET)) {
            mSawSound.playSound();
            mContext.getKoalaTwo().die();
            mContext.setGameToFinish();
        }
        if (mContext.getKoalaThree().collision(getX(), getY(), getWidth() - SAW_BOUNDS_OFFSET, getHeight() - SAW_BOUNDS_OFFSET)) {
            mSawSound.playSound();
            mContext.getKoalaThree().die();
            mContext.setGameToFinish();
        }
    }

    private void moveSaw() {
        if (getY() <= mLowerMoveBound && !mSawMoveUp) {
            setY(getY() + 1);
            if (getY() == mLowerMoveBound) {
                mSawMoveUp = true;
            }
        }
        if (getY() >= mSawInitialPosition && mSawMoveUp) {
            setY(getY() - 1);
            if (getY() == mSawInitialPosition) {
                mSawMoveUp = false;
            }
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        moveSaw();
        update();
        g.drawImage(getImage(), mXPosition, mYPosition, obs);
    }
}
