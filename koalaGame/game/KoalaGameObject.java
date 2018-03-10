package koalaGame.game;

import game.GameObject;
import java.io.IOException;

public class KoalaGameObject extends GameObject{


    public KoalaGameObject(String resourceLocation, int xPosition, int yPosition, int tileSize) throws IOException {

     super(resourceLocation, xPosition, yPosition, tileSize);
    }

    public void moveUp(int speed) {
        mYPosition -= speed;
    }

    public void moveDown(int speed) {
        mYPosition += speed;
    }

    public void moveLeft(int speed) {
        mXPosition -= speed;
    }

    public void moveRight(int speed) {
        mXPosition += speed;
    }

}
