package koalaGame.game;

import game.graphics.Sprite;
import game.*;

import java.awt.*;
import java.awt.image.ImageObserver;

import java.util.Observable;
import java.util.Observer;
import java.io.IOException;
import java.awt.event.KeyEvent;

public class Koala extends KoalaGameObject implements Observer {
    private KoalaGameWorld mContext;
    private boolean mIsDestroyed;
    private boolean mIsRemoved;
    private boolean mSetPosition;
    private boolean mIsCollided;
    private static final String DEAD_KOALA_URI = "resources/koala_dead.png";
    private Sprite mDeadKoala;
    private static final int KOALA_SPEED = 5;
    private static final int KOALA_BOUNDS_OFFSET = 5;
    protected static int sKoalaCount = 0;
    protected boolean mIsMovingUp, mIsMovingDown, mIsMovingLeft, mIsMovingRight;


    public Koala(KoalaGameWorld context, String resource, int xPosition, int yPosition) throws IOException {
        super(resource, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
    }

    public void die() {
        try {
            mDeadKoala = new Sprite(DEAD_KOALA_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIsDestroyed = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        GameKeyEvent KoalaKeyEvent = (GameKeyEvent) arg;
        CheckCollision();

        switch (KoalaKeyEvent.getKey()) {
            case KeyEvent.VK_LEFT:
                if (KoalaKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mIsMovingLeft = true;
                    if (!mSetPosition) {
                        mCurrentSpriteFrame = getSprite().getFrameLength() - 1;
                        mSetPosition = true;
                    }
                    if(!mIsCollided) {
                        moveLeft(KOALA_SPEED);
                    }
                    if (mCurrentSpriteFrame >= 31) {
                        mCurrentSpriteFrame = 24;
                    } else {
                        mCurrentSpriteFrame++;
                    }
                } else {
                    resetKoalaPosition();
                    mSetPosition = false;
                }
                break;
            case KeyEvent.VK_UP:
                if (KoalaKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mIsMovingUp = true;
                    if (!mSetPosition) {
                        mCurrentSpriteFrame = getSprite().getFrameLength() - 9;
                        mSetPosition = true;
                    }
                    if(!mIsCollided) {
                        moveUp(KOALA_SPEED);
                    }
                    if (mCurrentSpriteFrame >= 23) {
                        mCurrentSpriteFrame = 17;
                    } else {
                        mCurrentSpriteFrame++;
                    }
                } else {
                    resetKoalaPosition();
                    mSetPosition = false;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (KoalaKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mIsMovingRight = true;
                    if (!mSetPosition) {
                        mCurrentSpriteFrame = getSprite().getFrameLength() / 2 - 1;
                        mSetPosition = true;
                    }
                    if(!mIsCollided) {
                        moveRight(KOALA_SPEED);
                    }
                    if (mCurrentSpriteFrame >= 15) {
                        mCurrentSpriteFrame = 8;
                    } else {
                        mCurrentSpriteFrame++;
                    }
                } else {
                    resetKoalaPosition();
                    mSetPosition = false;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (KoalaKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mIsMovingDown = true;
                    if (!mSetPosition) {
                        mCurrentSpriteFrame = getSprite().getFrameLength() / 4 - 1;
                        mSetPosition = true;
                    }
                    if(!mIsCollided) {
                        moveDown(KOALA_SPEED);
                    }
                    if (mCurrentSpriteFrame >= 7) {
                        mCurrentSpriteFrame = 0;
                    } else {
                        mCurrentSpriteFrame++;
                    }
                } else {
                    resetKoalaPosition();
                    mSetPosition = false;
                }
                break;
            case KeyEvent.VK_Q:
                mContext.destroy();
                break;
        }
        mIsCollided = false;
    }

    public void CheckCollision() {
        if (mContext.getKoalaOne().collision(mContext.getKoalaTwo().getX(), mContext.getKoalaTwo().getY(), mContext.getKoalaTwo().getWidth(), mContext.getKoalaTwo().getHeight())) {
           if (mIsMovingRight || mIsMovingLeft) {
               if ( mContext.getKoalaOne().getX() > mXPosition ) {
                   mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() + KOALA_BOUNDS_OFFSET);
                   mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() - KOALA_BOUNDS_OFFSET);
               } else if ( mContext.getKoalaOne().getX() < mXPosition ) {
                   mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() - KOALA_BOUNDS_OFFSET);
                   mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() + KOALA_BOUNDS_OFFSET);
               }
           }
           if (mIsMovingDown || mIsMovingUp) {
               if ( mContext.getKoalaOne().getY() > mYPosition ) {
                   mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() + KOALA_BOUNDS_OFFSET);
                   mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() - KOALA_BOUNDS_OFFSET);
               } else if ( mContext.getKoalaOne().getY() < mYPosition ) {
                   mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() - KOALA_BOUNDS_OFFSET);
                   mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() + KOALA_BOUNDS_OFFSET);
               }
           }
           mIsCollided = true;
        }
        if (mContext.getKoalaOne().collision(mContext.getKoalaThree().getX(), mContext.getKoalaThree().getY(), mContext.getKoalaThree().getWidth(), mContext.getKoalaThree().getHeight())) {
            if (mIsMovingRight || mIsMovingLeft) {
                if ( mContext.getKoalaOne().getX() > mXPosition ) {
                    mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() + KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() - KOALA_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaOne().getX() < mXPosition ) {
                    mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() - KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() + KOALA_BOUNDS_OFFSET);
                }
            }
            if (mIsMovingDown || mIsMovingUp) {
                if ( mContext.getKoalaOne().getY() > mYPosition ) {
                    mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() + KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() - KOALA_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaOne().getY() < mYPosition ) {
                    mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() - KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() + KOALA_BOUNDS_OFFSET);
                }
            }
            mIsCollided = true;
        }
        if (mContext.getKoalaTwo().collision(mContext.getKoalaThree().getX(), mContext.getKoalaThree().getY(), mContext.getKoalaThree().getWidth(), mContext.getKoalaThree().getHeight())) {
            if (mIsMovingRight || mIsMovingLeft) {
                if ( mContext.getKoalaTwo().getX() > mXPosition ) {
                    mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() + KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() - KOALA_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaTwo().getX() < mXPosition ) {
                    mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() - KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() + KOALA_BOUNDS_OFFSET);
                }
            }
            if (mIsMovingDown || mIsMovingUp) {
                if ( mContext.getKoalaTwo().getY() > mYPosition ) {
                    mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() + KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() - KOALA_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaTwo().getY() < mYPosition ) {
                    mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() - KOALA_BOUNDS_OFFSET);
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() + KOALA_BOUNDS_OFFSET);
                }
            }
            mIsCollided = true;
        }
    }

    public boolean isDead() {
        return mIsDestroyed;
    }

    public boolean isRemoved() {
        return mIsRemoved;
    }

    public void remove() {
        this.setWidth(0);
        this.setHeight(0);
        mIsRemoved = true;
        sKoalaCount += 1;
    }

    private void resetKoalaPosition() {
        mCurrentSpriteFrame = (getSprite().getFrameLength() / 4 - 1);
        mIsMovingUp = false;
        mIsMovingDown = false;
        mIsMovingLeft = false;
        mIsMovingRight = false;
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (!mIsRemoved) {
            if (!mIsDestroyed) {
                g.drawImage(getSprite().getFrame(mCurrentSpriteFrame), mXPosition, mYPosition, obs);
            } else {
                g.drawImage(mDeadKoala.getFrame(0), mXPosition, mYPosition, obs);
            }
        }
    }
}
