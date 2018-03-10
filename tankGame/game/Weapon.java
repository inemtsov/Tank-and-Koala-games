package tankGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

public class Weapon extends TankGameObject {
    private static final int WEAPON_SPEED = 12;
    private  static final String BULLET_SHOT_URI = "resources/Bullet_shot.wav";

    private GameSounds mShotSound;
    private TankGameWorld mContext;
    private boolean isMoving;

    public Weapon(TankGameWorld context, String resource, int xPosition, int yPosition) throws IOException {
        super(resource, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_SMALL);
        mContext = context;
    }

    public void update() {
        if (isMoving) {
            if(mContext.getPlayerOne().collision(getX(), getY(), getWidth(), getHeight())) {
                mContext.getPlayerOne().doDamage();
                isMoving = false;
            }else if(mContext.getPlayerTwo().collision(getX(), getY(), getWidth(), getHeight())) {
                mContext.getPlayerTwo().doDamage();
                isMoving = false;
            }else {
                ArrayList<TankGameWall> wallList = mContext.getGameWalls();
                for(TankGameWall wall : wallList) {
                    Rectangle gameRect = wall.getWallRectangle();
                    if(collision((int) gameRect.getX(), (int) gameRect.getY(), (int) gameRect.getWidth(), (int) gameRect.getHeight())
                        && !wall.isDestroyed()) {
                        if(wall.isDestructible()) {
                            wall.setDestroyed(true);
                        }
                        isMoving = false;
                    }
                }
            }
        }
    }

    public void setBulletPosition(int xPosition, int yPosition, int currentFrame) {
        if (!isMoving) {
            mCurrentSpriteFrame = currentFrame;
            double degrees = Math.toRadians((360 / getSprite().getFrameLength()) * currentFrame);
            int newX = xPosition + (int) ((70 * Math.cos(degrees)));
            int newY = yPosition - (int) ((70 * Math.sin(degrees)));
            setX(newX);
            setY(newY);
        }
    }

    public void fireBullet() {
        if (!mContext.getPlayerTwo().isDestroyed() &&  !mContext.getPlayerOne().isDestroyed()) {
            if (!isMoving) {
                mShotSound = new GameSounds(BULLET_SHOT_URI);
                mShotSound.playSound();
            }
            isMoving = true;
            fireBullet(WEAPON_SPEED);
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (isMoving) {
            update();
            fireBullet(WEAPON_SPEED);
            g.drawImage(getSprite().getFrame(mCurrentSpriteFrame), mXPosition, mYPosition, obs);
        }
    }
}
