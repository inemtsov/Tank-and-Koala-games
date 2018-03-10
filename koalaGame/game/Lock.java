package koalaGame.game;

import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Lock extends KoalaGameObject {

    private KoalaGameWorld mContext;
    private boolean mIsOpen;
    private static final int LOCK_OFFSET_BOUNDS = 5;

    public Lock(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
    }

    public void update() {
        if (mContext.getKoalaOne().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaOne().getX() < getX()) {
                mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() - LOCK_OFFSET_BOUNDS);
            }
            if (mContext.getKoalaOne().getX() > getX()) {
                mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() + LOCK_OFFSET_BOUNDS);
            }
        }
        if (mContext.getKoalaTwo().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaTwo().getX() < getX()) {
                mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() - LOCK_OFFSET_BOUNDS);
            }
            if (mContext.getKoalaTwo().getX() > getX()) {
                mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() + LOCK_OFFSET_BOUNDS);
            }
        }
        if (mContext.getKoalaThree().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaThree().getX() < getX()) {
                mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() - LOCK_OFFSET_BOUNDS);
            }
            if (mContext.getKoalaThree().getX() > getX()) {
                mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() + LOCK_OFFSET_BOUNDS);
            }
        }
    }

    public void openLock() {
        mIsOpen = true;
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (!mIsOpen) {
            update();
            g.drawImage(getImage(), mXPosition, mYPosition, obs);
        }
    }
}
