package koalaGame.game;

import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class KoalaGameWall extends KoalaGameObject {

    private KoalaGameWorld mContext;
    private Rectangle mWallRectangle;
    private static final int WALL_BOUNDS_OFFSET = 5;

    public KoalaGameWall(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
        mWallRectangle = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void update() {
        if ( mContext.getKoalaOne().collision(getX(), getY(), getWidth(), getHeight()) ) {
            if ( mContext.getKoalaOne().mIsMovingRight || mContext.getKoalaOne().mIsMovingLeft ) {
                if ( mContext.getKoalaOne().getX() > getX() ) {
                    mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaOne().getX() < getX() ) {
                    mContext.getKoalaOne().setX(mContext.getKoalaOne().getX() - WALL_BOUNDS_OFFSET);
                }
            }
            if ( mContext.getKoalaOne().mIsMovingDown || mContext.getKoalaOne().mIsMovingUp ) {
                if ( mContext.getKoalaOne().getY() > getY() ) {
                    mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaOne().getY() < getY() ) {
                    mContext.getKoalaOne().setY(mContext.getKoalaOne().getY() - WALL_BOUNDS_OFFSET);
                }
            }
        }
        if ( mContext.getKoalaTwo().collision(getX(), getY(), getWidth(), getHeight()) ) {
            if ( mContext.getKoalaTwo().mIsMovingRight || mContext.getKoalaTwo().mIsMovingLeft ) {
                if ( mContext.getKoalaTwo().getX() > getX() ) {
                    mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaTwo().getX() < getX() ) {
                    mContext.getKoalaTwo().setX(mContext.getKoalaTwo().getX() - WALL_BOUNDS_OFFSET);
                }
            }
            if ( mContext.getKoalaTwo().mIsMovingDown || mContext.getKoalaTwo().mIsMovingUp ) {
                if ( mContext.getKoalaTwo().getY() > getY() ) {
                    mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaTwo().getY() < getY() ) {
                    mContext.getKoalaTwo().setY(mContext.getKoalaTwo().getY() - WALL_BOUNDS_OFFSET);
                }
            }
        }
        if ( mContext.getKoalaThree().collision(getX(), getY(), getWidth(), getHeight()) ) {
            if ( mContext.getKoalaThree().mIsMovingRight || mContext.getKoalaThree().mIsMovingLeft ) {
                if ( mContext.getKoalaThree().getX() > getX() ) {
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaThree().getX() < getX() ) {
                    mContext.getKoalaThree().setX(mContext.getKoalaThree().getX() - WALL_BOUNDS_OFFSET);
                }
            }
            if ( mContext.getKoalaThree().mIsMovingDown || mContext.getKoalaThree().mIsMovingUp ) {
                if ( mContext.getKoalaThree().getY() > getY() ) {
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() + WALL_BOUNDS_OFFSET);
                } else if ( mContext.getKoalaThree().getY() < getY() ) {
                    mContext.getKoalaThree().setY(mContext.getKoalaThree().getY() - WALL_BOUNDS_OFFSET);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        update();
        g.drawImage(getImage(), mXPosition, mYPosition, obs);
    }
}
