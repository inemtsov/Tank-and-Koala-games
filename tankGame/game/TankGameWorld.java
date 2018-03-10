package tankGame.game;

import game.graphics.Sprite;
import game.*;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TankGameWorld extends JApplet implements Runnable, WindowListener {
    private static final int MAP_WIDTH = 1240;
    private static final int MAP_HEIGHT = 1300;
    private static final int FRAME_WIDTH = MAP_WIDTH;
    private static final int FRAME_HEIGHT = MAP_HEIGHT / 2;
    private static final int MINI_MAP_WIDTH = MAP_WIDTH / 6;
    private static final int MINI_MAP_HEIGHT = MAP_HEIGHT / 6;
    private static final int MINI_MAP_X_POS = (FRAME_WIDTH / 2) - (MINI_MAP_WIDTH / 2);
    private static final int MINI_MAP_Y_POS = 400;

    private static final int WALL_REGULAR = 1;
    private static final int WALL_DESTRUCTIBLE = 2;
    private static final int PLAYER_ONE_START_POSITION = 3;
    private static final int PLAYER_TWO_START_POSITION = 4;
    private static final int HEALTH_BOOST = 5;
    private static final int GAME_SCORE = 3;
    private static final String BACKGROUND_IMAGE_URI = "resources/Background.png";
    private static final String LEVEL_FILE_URI = "resources/level.txt";
    private static final String WALL_REGULAR_URI = "resources/Blue_wall1.png";
    private static final String WALL_DESTRUCTIBLE_URI = "resources/Blue_wall2.png";
    private static final String TANK_ONE_URI = "resources/Tank_red_basic_strip60.png";
    private static final String TANK_TWO_URI = "resources/Tank_blue_basic_strip60.png";
    private static final String BACKGROUND_MUSIC_URI = "resources/BackgroundMusic.wav";
    private static final String TANK_ONE_WEAPON = "resources/Shell_basic_strip60.png";
    private static final String TANK_TWO_WEAPON = "resources/Shell_heavy_strip60.png";
    private static final String TANK_EXPLOSION_ANIMATION_URI = "resources/Explosion_small_strip6.png";
    private static final String HEALTH_BOOST_URI = "resources/redCross.png";

    private Player mTankOne;
    private Player mTankTwo;
    private static TankGameWorld sTankGameWorld;

    private Thread mUIThread;
    private GameBackground mFrameBackground;
    private Dimension mFrameDimension;
    private GameSounds mMusic;
    private ArrayList<TankGameWall> mTankGameWallList = new ArrayList<>();
    private ArrayList<HealthUp> mHealBoost = new ArrayList<>();

    private BufferedImage mMapImage;
    private BufferedImage mDisplayImage;
    private Sprite mExplosion;
    private Graphics2D mGraphics2d;
    private Graphics2D mDisplayGraphics;
    private GameKeyboardPanel mGameKeyboardPanel;
    private boolean mGameIsOver = false;
    private int mCurrentExplosionAnimationFrame;

    public static TankGameWorld getInstance() {
        if (sTankGameWorld == null) {
            sTankGameWorld = new TankGameWorld();
        }

        return sTankGameWorld;
    }

    private TankGameWorld() {
        try {
            mFrameBackground = new GameBackground(BACKGROUND_IMAGE_URI);
            mMusic = new GameSounds(BACKGROUND_MUSIC_URI);
            mExplosion = new Sprite(TANK_EXPLOSION_ANIMATION_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mFrameDimension = new Dimension(MAP_WIDTH, MAP_HEIGHT);
        loadLevelMap();
    }

    @Override
    public void init() {
        super.init();
        mGameKeyboardPanel = new GameKeyboardPanel(this, FRAME_WIDTH, FRAME_HEIGHT);
        mGameKeyboardPanel.setTitle("Russia vs. Germany");
        mGameKeyboardPanel.addWindowListener(this);
        mMusic.playSound();
        mMusic.playSoundContinuously();
        initObservers();
    }

    private void loadLevelMap() {
        LevelParser parser = new LevelParser(LEVEL_FILE_URI);
        int[][] mLevelMap = parser.parseLevelFile();

        if (mLevelMap.length <= 0) {
            return;
        }

        for (int i = 0; i < mLevelMap.length; i++) {
            int rowLength = mLevelMap[i].length;
            for (int j = 0; j < rowLength; j++) {

                try {
                    switch (mLevelMap[i][j]) {
                        case WALL_REGULAR:
                            mTankGameWallList.add(new TankGameWall(this, WALL_REGULAR_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM * j, Sprite.SPRITE_TILE_SIZE_MEDIUM * i, false));
                            break;
                        case WALL_DESTRUCTIBLE:
                            mTankGameWallList.add(new TankGameWall(this, WALL_DESTRUCTIBLE_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM * j, Sprite.SPRITE_TILE_SIZE_MEDIUM * i, true));
                            break;
                        case PLAYER_ONE_START_POSITION:
                            mTankOne = new Player(this, TANK_ONE_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM * j, Sprite.SPRITE_TILE_SIZE_MEDIUM * i);
                            mTankOne.setWeapon(new Weapon(this, TANK_ONE_WEAPON, mTankOne.getX(), mTankOne.getY()));
                            break;
                        case PLAYER_TWO_START_POSITION:
                            mTankTwo = new Player(this, TANK_TWO_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM * j, Sprite.SPRITE_TILE_SIZE_MEDIUM * i);
                            mTankTwo.setWeapon(new Weapon(this, TANK_TWO_WEAPON, mTankTwo.getX(), mTankTwo.getY()));
                            break;
                        case HEALTH_BOOST:
                            mHealBoost.add(new HealthUp(this, HEALTH_BOOST_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM * j, Sprite.SPRITE_TILE_SIZE_MEDIUM * i));
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initObservers() {
        GameKeyEvent mTankOneKeyEvent = new GameKeyEvent();
        GameKeyEvent mTankTwoKeyEvent = new GameKeyEvent();

        mTankOneKeyEvent.addObserver(mTankOne);
        mTankTwoKeyEvent.addObserver(mTankTwo);

        GameKeyAdapter mTankOneAdapter = new GameKeyAdapter(mTankOneKeyEvent);
        GameKeyAdapter mTankTwoAdapter = new GameKeyAdapter(mTankTwoKeyEvent);

        List<GameKeyAdapter> adapters = new ArrayList<>();
        adapters.add(mTankOneAdapter);
        adapters.add(mTankTwoAdapter);
        mGameKeyboardPanel.setKeyAdapters(adapters);
    }

    @Override
    public void start() {
        super.start();
        mUIThread = new Thread(this);
        mUIThread.setPriority(Thread.MIN_PRIORITY);
        mUIThread.start();
    }

    @Override
    public void paint(Graphics graphics) {
        if (mMapImage == null) {
            mMapImage = (BufferedImage) createImage(MAP_WIDTH, MAP_HEIGHT);
            mDisplayImage = (BufferedImage) createImage(MAP_WIDTH, MAP_HEIGHT);
            mGraphics2d = mMapImage.createGraphics();
            mDisplayGraphics = mDisplayImage.createGraphics();

            // Set initial tank positions
            mTankOne.setInitialSpriteFrame(mGraphics2d, mTankOne.getSprite().frameCount() / 2);
            mTankOne.getWeapon().setInitialSpriteFrame(mGraphics2d, 0);
            mTankTwo.setInitialSpriteFrame(mGraphics2d, 0);
            mTankTwo.getWeapon().setInitialSpriteFrame(mGraphics2d, 0);
        }
        updateFrame();
        graphics.drawImage(mDisplayImage, 0, 0, this);
    }

    public void updateFrame() {
        checkIfPlayerDestroyed();

        if (!mGameIsOver) {
            updateMap();
            renderMap();
            drawWalls();
            drawHealthBoost();
            drawPlayers();

            int minTankOneX = Math.min(MAP_WIDTH / 2, mTankOne.getX() - (MAP_WIDTH / 8));
            int maxTankOneX = Math.max(0, minTankOneX);
            int minTankOneY = Math.min(MAP_HEIGHT / 2, mTankOne.getY() - (MAP_HEIGHT / 8));
            int maxTankOneY = Math.max(0, minTankOneY);

            int minTankTwoX = Math.min(MAP_WIDTH / 2, mTankTwo.getX() - (MAP_WIDTH / 8));
            int maxTankTwoX = Math.max(0, minTankTwoX);
            int minTankTwoY = Math.min(MAP_HEIGHT / 2, mTankTwo.getY() - (MAP_HEIGHT / 8));
            int maxTankTwoY = Math.max(0, minTankTwoY);

            BufferedImage leftImg = mMapImage.getSubimage(maxTankOneX, maxTankOneY, FRAME_WIDTH / 2, FRAME_HEIGHT);
            BufferedImage rightImg = mMapImage.getSubimage(maxTankTwoX, maxTankTwoY, FRAME_WIDTH / 2, FRAME_HEIGHT);

            BufferedImage temp = mMapImage.getSubimage(0, 0, MAP_WIDTH, MAP_HEIGHT);
            Image miniMap = temp.getScaledInstance(MINI_MAP_WIDTH, MINI_MAP_HEIGHT, Image.SCALE_SMOOTH);

            String tankOneInfo = "Health " + mTankOne.getHealth() + " Lives: " + mTankOne.getLives();
            String tankTwoInfo = "Health " + mTankTwo.getHealth() + " Lives: " + mTankTwo.getLives();

            mDisplayGraphics.drawImage(leftImg, 0, 0, this);
            mDisplayGraphics.drawImage(rightImg, FRAME_WIDTH / 2, 0, this);
            mDisplayGraphics.drawImage(miniMap, MINI_MAP_X_POS, MINI_MAP_Y_POS, this);
            mDisplayGraphics.drawString(tankOneInfo, FRAME_WIDTH / 4, FRAME_HEIGHT - 30);
            mDisplayGraphics.drawString(tankTwoInfo, 3 * (FRAME_WIDTH / 4), FRAME_HEIGHT - 30);

            if (mTankOne.getCurrentHealthImage() != null && mTankTwo.getCurrentHealthImage() != null) {
                Image currentImage = mTankOne.getCurrentHealthImage();
                mDisplayGraphics.drawImage(mTankOne.getCurrentHealthImage(), (FRAME_WIDTH / 4) - currentImage.getWidth(this) - 20, FRAME_HEIGHT - 55, this);
                currentImage = mTankTwo.getCurrentHealthImage();
                mDisplayGraphics.drawImage(mTankTwo.getCurrentHealthImage(), 3 * (FRAME_WIDTH / 4) - currentImage.getWidth(this) - 20, FRAME_HEIGHT - 55, this);
            }
        }
    }

    private void checkIfPlayerDestroyed() {
        Font stringFont = new Font("SansSerif", Font.PLAIN, 18);
        mDisplayGraphics.setFont(stringFont);
        mDisplayGraphics.setColor(Color.white);
        if (mTankOne.isDestroyed()) {
            if (mCurrentExplosionAnimationFrame < mExplosion.getFrameLength()) {
                mTankOne.setImage(mExplosion.getFrame(mCurrentExplosionAnimationFrame));
                mCurrentExplosionAnimationFrame++;
            } else if (mTankTwo.getScore() == GAME_SCORE) {
                mGameIsOver = true;
                stringFont = new Font("SansSerif", Font.BOLD, 30);
                mDisplayGraphics.setFont(stringFont);
                mDisplayGraphics.setColor(Color.white);
                mDisplayGraphics.drawString(" Player 2 Won!  Result: " + mTankOne.getScore() + " - " + mTankTwo.getScore(), 170, 220);
            } else if (mTankOne.isDestroyed() && !(mCurrentExplosionAnimationFrame < mExplosion.getFrameLength())) {
                mTankOne.setHealth(Player.PLAYER_HEALTH);
                mTankOne.setDestroyed(false);
                mCurrentExplosionAnimationFrame = 0;
            }
        } else if (mTankTwo.isDestroyed()) {
            if (mCurrentExplosionAnimationFrame < mExplosion.getFrameLength()) {
                mTankTwo.setImage(mExplosion.getFrame(mCurrentExplosionAnimationFrame));
                mCurrentExplosionAnimationFrame++;
            } else if (mTankOne.getScore() == GAME_SCORE) {
                mGameIsOver = true;
                stringFont = new Font("SansSerif", Font.BOLD, 30);
                mDisplayGraphics.setFont(stringFont);
                mDisplayGraphics.setColor(Color.white);
                mDisplayGraphics.drawString("Player 1 Won!  Result: " + mTankOne.getScore() + " - " + mTankTwo.getScore(), 170, 220);
            } else if (mTankTwo.isDestroyed() && !(mCurrentExplosionAnimationFrame < mExplosion.getFrameLength())) {
                mTankTwo.setHealth(Player.PLAYER_HEALTH);
                mTankTwo.setDestroyed(false);
                mCurrentExplosionAnimationFrame = 0;
            }
        }
    }

    private void updateMap() {
        for (TankGameWall wall : mTankGameWallList) {
            wall.update();
        }
    }

    private void renderMap() {
        int backgroundWidth = mFrameBackground.getWidth();
        int backgroundHeight = mFrameBackground.getHeight();

        int tileWidth = MAP_WIDTH / backgroundWidth;
        int tileHeight = MAP_HEIGHT / backgroundHeight;

        for (int i = -1; i <= tileHeight; i++) {
            for (int j = 0; j <= tileWidth; j++) {
                mGraphics2d.drawImage(mFrameBackground.getImage(), j * backgroundWidth, i * backgroundHeight, backgroundWidth, backgroundHeight, this);
            }
        }

    }

    private void drawWalls() {
        for (TankGameWall wall : mTankGameWallList) {
            wall.draw(mGraphics2d, this);
        }
    }

    private void drawHealthBoost() {
        for (HealthUp health : mHealBoost) {
            health.draw(mGraphics2d, this);
        }
    }

    private void drawPlayers() {
        if (mTankOne != null && mTankTwo != null) {
            mTankOne.draw(mGraphics2d, this);
            mTankOne.getWeapon().draw(mGraphics2d, this);
            mTankTwo.draw(mGraphics2d, this);
            mTankTwo.getWeapon().draw(mGraphics2d, this);
        }
    }

    public Player getPlayerOne() {
        return mTankOne;
    }

    public Player getPlayerTwo() {
        return mTankTwo;
    }

    @Override
    public Dimension getPreferredSize() {
        return mFrameDimension;
    }

    @Override
    public void destroy() {
        super.destroy();
        mMusic.stopSound();
        mUIThread.interrupt();
        mUIThread = null;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (mUIThread == currentThread) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }
            repaint();
        }
    }

    public ArrayList<TankGameWall> getGameWalls() {
        return mTankGameWallList;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        destroy();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}