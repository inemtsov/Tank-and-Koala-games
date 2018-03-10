package tankGame.game;

import game.*;
import game.graphics.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Player extends TankGameObject implements Observer {
    public static final int PLAYER_HEALTH = 80;
    public static final int WEAPON_DAMAGE = 20;
    public static final int PLAYER_LIVES = 3;
    private static final int TANK_SPEED = 5;
    private static final int TANK_BOUNDS_OFFSET = 4;
    private static final String TANK_EXPLOSION_URI = "resources/TankExplosion.wav";
    private static final String HEALTH_BAR_FULL = "resources/Health_0.png";
    private static final String HEALTH_BAR_HIGH = "resources/Health_1.png";
    private static final String HEALTH_BAR_HALF = "resources/Health_2.png";
    private static final String HEALTH_BAR_LOW = "resources/Health_3.png";

    private int mLives;
    private int mScore;
    private int mHealth;
    private boolean mIsDestroyed;
    private boolean mTankOneCanFire = true;
    private boolean mTankTwoCanFire = true;
    private GameSounds mExplosionSound;
    private Weapon mWeapon;
    private List<BufferedImage> mHealthStatus = new ArrayList<>(4);
    private TankGameWorld mContext;

    public Player(TankGameWorld context, String resource, int xPosition, int yPosition) throws IOException {
        super(resource, xPosition, yPosition, Sprite.SPRITE_TILE_SIZE_LARGE);
        mContext = context;
        mLives = PLAYER_LIVES;
        mHealth = PLAYER_HEALTH;
        mIsDestroyed = false;
        mExplosionSound = new GameSounds(TANK_EXPLOSION_URI);
        initHealthStatus();
    }

    private void initHealthStatus() {
        try {
            mHealthStatus.add(ImageIO.read(new File(HEALTH_BAR_LOW)));
            mHealthStatus.add(ImageIO.read(new File(HEALTH_BAR_HALF)));
            mHealthStatus.add(ImageIO.read(new File(HEALTH_BAR_HIGH)));
            mHealthStatus.add(ImageIO.read(new File(HEALTH_BAR_FULL)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCurrentHealthImage() {
        int currentHealthIndex = Math.max(0, (mHealth / WEAPON_DAMAGE) - 1);
        return mHealthStatus.get(currentHealthIndex);
    }

    public void addScore() {
        mScore += 1;
    }

    public int getScore() {
        return mScore;
    }

    public void setHealth(int health) {
        mHealth = health;
    }

    public int getHealth() {
        return mHealth;
    }

    public void doDamage() {
        mHealth -= WEAPON_DAMAGE;
        if (mHealth <= 0) {
            die();
            mLives -= 1 ;
        }
    }

    public void die() {
        mExplosionSound.playSound();
        mIsDestroyed = true;
        if(mContext.getPlayerOne().isDestroyed()) {
          mContext.getPlayerTwo().addScore();
        } else {
          mContext.getPlayerOne().addScore();
        }
    }

    public void setWeapon(Weapon bullet) {
      mWeapon = bullet;
    }

    public Weapon getWeapon() {
      return mWeapon;
    }

    public void update() {
        if (mContext.getPlayerOne().collision(mContext.getPlayerTwo().getX(), mContext.getPlayerTwo().getY(), mContext.getPlayerTwo().getWidth(), mContext.getPlayerTwo().getHeight())) {
            if (mContext.getPlayerOne().getX() > mXPosition) {
                mContext.getPlayerOne().setX(mContext.getPlayerOne().getX() + TANK_BOUNDS_OFFSET);
                mContext.getPlayerTwo().setX(mContext.getPlayerTwo().getX() - TANK_BOUNDS_OFFSET);

            } else if (mContext.getPlayerOne().getX() < mXPosition) {
                mContext.getPlayerOne().setX(mContext.getPlayerOne().getX() - TANK_BOUNDS_OFFSET);
                mContext.getPlayerTwo().setX(mContext.getPlayerTwo().getX() + TANK_BOUNDS_OFFSET);
            }
            if (mContext.getPlayerOne().getY() > mYPosition) {
                mContext.getPlayerOne().setY(mContext.getPlayerOne().getY() + TANK_BOUNDS_OFFSET);
                mContext.getPlayerTwo().setY(mContext.getPlayerTwo().getY() - TANK_BOUNDS_OFFSET);
            } else if (mContext.getPlayerOne().getY() < mYPosition) {
                mContext.getPlayerOne().setY(mContext.getPlayerOne().getY() - TANK_BOUNDS_OFFSET);
                mContext.getPlayerTwo().setY(mContext.getPlayerTwo().getY() + TANK_BOUNDS_OFFSET);
            }
        }
        moveForward(TANK_SPEED);
        moveBackward(TANK_SPEED);
        rotateLeft();
        rotateRight();
    }

    public int getLives() {
        return mLives;
    }
    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    public void setDestroyed(boolean sd) {
      mIsDestroyed = sd;
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (!mIsDestroyed) {
            update();
            mWeapon.setBulletPosition(mXPosition, mYPosition, mCurrentSpriteFrame);
            g.drawImage(getSprite().getFrame(mCurrentSpriteFrame), mXPosition, mYPosition, obs);
        } else {
          super.draw(g, obs);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        GameKeyEvent gameKeyEvent = (GameKeyEvent) arg;

        switch (gameKeyEvent.getKey()) {
            case KeyEvent.VK_A:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerOne().mIsRotatingRight = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerOne().mIsRotatingRight = true;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerTwo().mIsRotatingRight = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerTwo().mIsRotatingRight = true;
                }
                break;
            case KeyEvent.VK_W:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerOne().mIsMovingForward = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerOne().mIsMovingForward = true;
                }
                break;
            case KeyEvent.VK_UP:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerTwo().mIsMovingForward = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerTwo().mIsMovingForward = true;
                }
                break;
            case KeyEvent.VK_D:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerOne().mIsRotatingLeft = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerOne().mIsRotatingLeft = true;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerTwo().mIsRotatingLeft = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerTwo().mIsRotatingLeft = true;
                }
                break;
            case KeyEvent.VK_S:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerOne().mIsMovingBackward = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerOne().mIsMovingBackward = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mContext.getPlayerTwo().mIsMovingBackward = false;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED) {
                    mContext.getPlayerTwo().mIsMovingBackward = true;
                }
                break;
            case KeyEvent.VK_SPACE:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mTankOneCanFire = true;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED && mTankOneCanFire) {
                    mContext.getPlayerOne().getWeapon().fireBullet();
                    mTankOneCanFire = false;
                }
                break;
            case KeyEvent.VK_ENTER:
                if (gameKeyEvent.getId() == KeyEvent.KEY_RELEASED) {
                    mTankTwoCanFire = true;
                } else if (gameKeyEvent.getId() == KeyEvent.KEY_PRESSED && mTankTwoCanFire) {
                    mContext.getPlayerTwo().getWeapon().fireBullet();
                    mTankTwoCanFire = false;
                }
                break;
        }
    }
}
