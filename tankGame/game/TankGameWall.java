package tankGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class TankGameWall extends TankGameObject {
    private static final int WALL_DESTRUCTED_TIME = 200;
    private static final String WALL_EXPLOSION_URI = "resources/WallExplosion.wav";
    private static final int TANK_BOUNDS_OFFSET = 4;

    private boolean mIsDestructible;
    private boolean mIsDestroyed;
    private int mCount;
    private TankGameWorld mContext;
    private GameSounds mExplosionSound;
    private Rectangle mWallRectangle;

    public TankGameWall(TankGameWorld context, String resourceImageLocation, int xPosition, int yPosition, boolean isDestructible) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_LARGE);
        mContext = context;
        mExplosionSound = new GameSounds(WALL_EXPLOSION_URI);
        mIsDestructible = isDestructible;
        mWallRectangle = new Rectangle(getX(), getY(), getWidth(), getHeight() );
    }

    public boolean isDestroyed() {
        return (mIsDestroyed && mIsDestructible);
    }

    public Rectangle getWallRectangle() {
        return mWallRectangle;
    }

    public boolean isDestructible() {
        return mIsDestructible;
    }

    public void setDestroyed(boolean s) {
        mExplosionSound.playSound();
        mIsDestroyed = s;
        mCount = 0;
    }

    public void update() {
        if (mIsDestroyed && mCount < WALL_DESTRUCTED_TIME) {
            mCount++;
        } else {
            mIsDestroyed = false;
            mCount = 0;
        }
        if(mContext.getPlayerOne().collision(getX(), getY(), getWidth(), getHeight()) && !isDestroyed()) {
            if(mContext.getPlayerOne().getX() > getX()) {
                mContext.getPlayerOne().setX(mContext.getPlayerOne().getX() + TANK_BOUNDS_OFFSET);
            }else if(mContext.getPlayerOne().getX() < getX()) {
                mContext.getPlayerOne().setX(mContext.getPlayerOne().getX() - TANK_BOUNDS_OFFSET);
            }
            if(mContext.getPlayerOne().getY() > getY()) {
                mContext.getPlayerOne().setY(mContext.getPlayerOne().getY() + TANK_BOUNDS_OFFSET);
            }else if(mContext.getPlayerOne().getY() < getY()) {
                mContext.getPlayerOne().setY(mContext.getPlayerOne().getY() - TANK_BOUNDS_OFFSET);
            }
        }
        if(mContext.getPlayerTwo().collision(getX(), getY(), getWidth(), getHeight()) && !isDestroyed()) {
            if(mContext.getPlayerTwo().getX() > getX()) {
                mContext.getPlayerTwo().setX(mContext.getPlayerTwo().getX() + TANK_BOUNDS_OFFSET);
            }else if(mContext.getPlayerTwo().getX() < getX()) {
                mContext.getPlayerTwo().setX(mContext.getPlayerTwo().getX() - TANK_BOUNDS_OFFSET);
            }
            if(mContext.getPlayerTwo().getY() > getY()) {
                mContext.getPlayerTwo().setY(mContext.getPlayerTwo().getY() + TANK_BOUNDS_OFFSET);
            }else if(mContext.getPlayerTwo().getY() < getY()) {
                mContext.getPlayerTwo().setY(mContext.getPlayerTwo().getY() - TANK_BOUNDS_OFFSET);
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
