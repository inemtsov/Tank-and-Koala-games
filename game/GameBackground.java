package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class GameBackground {
    private BufferedImage mImage;
    private ImageObserver mObserver;
    private int mXPosition;
    private int mYPosition;

    public GameBackground(String imageUri) throws IOException {
        this(imageUri, null);
    }

    public GameBackground(String imageUri, ImageObserver observer) throws IOException {
        mImage = ImageIO.read(new File(imageUri));
        mObserver = observer;
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

    public int getWidth() {
        return mImage.getWidth();
    }

    public int getHeight() {
        return mImage.getHeight();
    }

    public BufferedImage getImage() {
        return mImage;
    }
}
