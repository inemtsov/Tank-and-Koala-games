package koalaGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class TNT extends KoalaGameObject {

    private GameSounds mExplosionSound;
    private final static String EXPLOSION_SOUND_URI = "resources/koala_explosion.wav";
    private KoalaGameWorld mContext;
    private boolean mExplodeTNT;
    private static final int TNT_BOUNDS_OFFSET = 5;

    public TNT(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
    }

    public void explode() {
        mExplodeTNT = true;
    }

    public boolean getExplosionStatus() {
        return mExplodeTNT;
    }

    public void update() {
        if (mContext.getKoalaOne().collision(getX(), getY(), getWidth() - TNT_BOUNDS_OFFSET, getHeight() - TNT_BOUNDS_OFFSET)) {
            boom();
            mContext.getKoalaOne().die();
        }
        if (mContext.getKoalaTwo().collision(getX(), getY(), getWidth() - TNT_BOUNDS_OFFSET, getHeight() - TNT_BOUNDS_OFFSET)) {
            boom();
            mContext.getKoalaTwo().die();
        }
        if (mContext.getKoalaThree().collision(getX(), getY(), getWidth() - TNT_BOUNDS_OFFSET, getHeight() - TNT_BOUNDS_OFFSET)) {
            boom();
            mContext.getKoalaThree().die();
        }
    }

    private void boom() {
        mExplosionSound = new GameSounds(EXPLOSION_SOUND_URI);
        mExplosionSound.playSound();
        explode();
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (!mExplodeTNT) {
            update();
            g.drawImage(getImage(), mXPosition, mYPosition, obs);
        } else {
            super.draw(g, obs);
        }
    }
}
