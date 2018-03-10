package tankGame.game;

import game.GameObject;
import java.io.IOException;

public class TankGameObject extends GameObject{

    public boolean mIsRotatingLeft;
    public boolean mIsRotatingRight;
    public boolean mIsMovingForward;
    public boolean mIsMovingBackward;

    public TankGameObject(String resourceLocation, int xPosition, int yPosition, int tileSize) throws IOException {
        super(resourceLocation, xPosition, yPosition, tileSize);
    }

    public void fireBullet(int speed) {
        double degrees = Math.toRadians((360 / getSprite().getFrameLength()) * mCurrentSpriteFrame);
        mXPosition += speed * Math.cos(degrees);
        mYPosition -= speed * Math.sin(degrees);
    }

    public void moveForward(int speed) {
        if (mIsMovingForward) {
            double degrees = Math.toRadians((360 / getSprite().getFrameLength()) * mCurrentSpriteFrame);
            mXPosition += speed * Math.cos(degrees);
            mYPosition -= speed * Math.sin(degrees);
        }
    }

    public void moveBackward(int speed) {
        if (mIsMovingBackward) {
            double degrees = Math.toRadians((360 / getSprite().getFrameLength()) * mCurrentSpriteFrame);
            mXPosition -= speed * Math.cos(degrees);
            mYPosition += speed * Math.sin(degrees);
        }
    }

    public void rotateLeft() {
        if (mIsRotatingLeft) {
            if (mCurrentSpriteFrame == 0) {
                mCurrentSpriteFrame = getSprite().getFrameLength() - 1;
            } else {
                mCurrentSpriteFrame--;
            }
        }
    }

    public void rotateRight() {
        if (mIsRotatingRight) {
            if (mCurrentSpriteFrame == (getSprite().getFrameLength() - 1)) {
                mCurrentSpriteFrame = 0;
            } else {
                mCurrentSpriteFrame++;
            }
        }
    }
}
