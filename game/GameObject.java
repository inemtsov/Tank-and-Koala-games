package game;

import game.graphics.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class GameObject {

    protected int mXPosition;
    protected int mYPosition;
    private int mWidth;
    private int mHeight;
    protected BufferedImage mImage;
    protected int mCurrentSpriteFrame;
    private Sprite mSprite;
    private Rectangle mRectangle;

    public GameObject(String resourceLocation, int xPosition, int yPosition, int tileSize) throws IOException {
        mImage = ImageIO.read(new File(resourceLocation));
        mSprite = new Sprite(resourceLocation, tileSize);
        mXPosition = xPosition;
        mYPosition = yPosition;
        mWidth = mImage.getWidth();
        mHeight = mImage.getHeight();
        mRectangle = new Rectangle();
    }

    public void setX(int xPosition) {
        mXPosition = xPosition;
    }

    public int getX() {
        return mXPosition;
    }

    public void setY(int yPosition) {
        mYPosition = yPosition;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getY() {
        return mYPosition;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Sprite getSprite() {
        return mSprite;
    }

    public BufferedImage getImage() {
        return mImage;
    }

    public boolean collision(int xPosition, int yPosition, int width, int height) {
        mRectangle.setBounds(mXPosition, mYPosition, mWidth, mHeight);
        return mRectangle.intersects(new Rectangle(xPosition, yPosition, width, height));
    }

    public void setInitialSpriteFrame(Graphics g, int frameIndex) {
        mCurrentSpriteFrame = frameIndex;
        g.drawImage(mSprite.getFrame(frameIndex), mXPosition, mYPosition, null);
        mWidth = mImage.getWidth() / mSprite.frameCount();
    }

    public void setImage(BufferedImage image) {
        mImage = image;
    }

    public void draw(Graphics g, ImageObserver obs) {
        g.drawImage(mImage, mXPosition, mYPosition, obs);
    }
}
