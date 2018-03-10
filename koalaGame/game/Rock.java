package koalaGame.game;

import game.GameSounds;
import game.graphics.Sprite;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Rock extends KoalaGameObject {

    private KoalaGameWorld mContext;
    private boolean mIsRockMoved;
    private GameSounds mRockSound;
    private final static String ROCK_URI = "resources/koala_Rock.wav";

    public Rock(KoalaGameWorld context, String resourceImageLocation, int xPosition, int yPosition) throws IOException {
        super(resourceImageLocation, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        mContext = context;
    }
    //collision only works for one side, assuming the koala can only be on the right side.
    public void update() {
        if (mContext.getKoalaOne().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaOne().getX() > getX()) {
                this.setX(getX() - 40);
                mRockSound = new GameSounds(ROCK_URI);
                mRockSound.playSound();
            }
        }
        if (mContext.getKoalaTwo().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaTwo().getX() > getX()) {
                this.setX(getX() - 40);
                mRockSound = new GameSounds(ROCK_URI);
                mRockSound.playSound();
            }
        }
        if (mContext.getKoalaThree().collision(getX(), getY(), getWidth(), getHeight())) {
            if (mContext.getKoalaThree().getX() > getX()) {
                this.setX(getX() - 40);
                mRockSound = new GameSounds(ROCK_URI);
                mRockSound.playSound();
            }
        }
    }

    public void setRockToMove() {
        mIsRockMoved = true;
        setWidth(0);
        setHeight(0);
    }


    @Override
    public void draw(Graphics g, ImageObserver obs) {
        update();
        if (!mIsRockMoved) {
            g.drawImage(getImage(), mXPosition, mYPosition, obs);
        }
    }
}
