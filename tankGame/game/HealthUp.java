package tankGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class HealthUp extends TankGameObject {
    private static final int FULL_BAR = 80;
    private static final String HEALTH_BOOST_SOUND = "resources/Health.wav";

    private boolean mIsDestroyed;
    private TankGameWorld mContext;
    private GameSounds mBoost;

    public HealthUp(TankGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_LARGE);
        mContext = context;
        mBoost = new GameSounds(HEALTH_BOOST_SOUND);
    }

    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    public void setBoost(boolean s) {
        mBoost.playSound();
        mIsDestroyed = s;
    }

    public void update() {
        if(mContext.getPlayerOne().collision(getX(), getY(), getWidth(), getHeight()) && !isDestroyed()) {
            if(mContext.getPlayerOne().getHealth() != FULL_BAR) {
                mContext.getPlayerOne().setHealth(FULL_BAR);
                setBoost(true);
            }
        }
        if(mContext.getPlayerTwo().collision(getX(), getY(), getWidth(), getHeight()) && !isDestroyed()) {
            if(mContext.getPlayerTwo().getHealth() != FULL_BAR) {
                mContext.getPlayerTwo().setHealth(FULL_BAR);
                setBoost(true);
            }
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (!isDestroyed()) {
            update();
            g.drawImage(getImage(), mXPosition, mYPosition, obs);
        }
    }
}
