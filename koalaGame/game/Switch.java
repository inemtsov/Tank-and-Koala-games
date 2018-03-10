package koalaGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Switch extends KoalaGameObject {

    private GameSounds mLockSound;
    private final static String LOCK_URI = "resources/koala_lock.wav";
    private KoalaGameWorld mContext;
    private boolean mIsSwitched;

    public Switch(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
        mLockSound = new GameSounds(LOCK_URI);
    }

    public void update() {
        if (mContext.getRock().collision(getX(), getY(), getWidth() / 2, getHeight())) {
            mIsSwitched = true;
            mContext.getRock().setRockToMove();
        }
        if (mIsSwitched) {
            mContext.getLock().openLock();
            mLockSound.playSound();
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        update();
        if (!mIsSwitched) {
            g.drawImage(getSprite().getFrame(1), mXPosition, mYPosition, obs);
        } else {
            g.drawImage(getSprite().getFrame(0), mXPosition, mYPosition, obs);
        }
    }
}
