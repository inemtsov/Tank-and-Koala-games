package koalaGame.game;

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
import java.io.File;
import javax.imageio.ImageIO;

public class KoalaGameWorld extends JApplet implements Runnable, WindowListener {
    private static final int MAP_WIDTH = 1200;
    private static final int MAP_HEIGHT = 1200;
    private static final int FRAME_WIDTH = 960;
    private static final int FRAME_HEIGHT = MAP_HEIGHT / 2;
    private static final String BACKGROUND_IMAGE_URI = "resources/koala_background.png";
    private static final String WALL_REGULAR_URI = "resources/koala_wall2.png";
    private static final String LEVEL_FILE_URI = "resources/koala_level.txt";
    private static final String SAW_RIGHT_URI = "resources/koala_saw-2.png";
    private static final String SAW_LEFT_URI = "resources/koala_saw.png";
    private static final String LOCK_URI = "resources/koala_lock_blue.png";
    private static final String SWITCH_URI = "resources/koala_switch_blue_strip2.png";
    private static final String KOALA_STAND_URI = "resources/koala_basic_32.png";
    private static final String TNT_URI = "resources/koala_TNT.png";
    private static final String BACKGROUND_MUSIC_URI = "resources/koala_music.wav";
    private static final String FINAL_SOUND_URI = "resources/koala_Ta_Da.wav";
    private static final String EXIT_URI = "resources/koala_exit_red.png";
    private static final String GAME_OVER_LABEL_URI = "resources/koala_gameover.png";
    private static final String CONGRATS__LABEL_URI = "resources/koala_congratulation.png";
    private static final String RESCUED_LABEL_URI = "resources/koala_rescued.png";
    private static final String RESC_KOALA_URI = "resources/koala_sit.png";
    private static final String ROCK_URI = "resources/koala_rock.png";
    private final static String EXPLOSION_IMAGE_URI = "resources/koala_explosion_small_strip6.png";
    private static final int WALL_REGULAR = 1;
    private static final int TNT_POSITION = 2;
    private static final int KOALA_ONE_START_POSITION = 3;
    private static final int KOALA_TWO_START_POSITION = 4;
    private static final int KOALA_THREE_START_POSITION = 5;
    private static final int BLUE_EXIT_POSITION = 6;
    private static final int SAW_RIGHT_POSITION = 7;
    private static final int SWITCH_POSITION = 8;
    private static final int LOCK_POSITION = 9;
    private static final int ROCK_POSITION = 97; //ascii representation of 'a'
    private static final int SAW_LEFT_POSITION = 98; //ascii representation of 'b'
    private static KoalaGameWorld sGameWorld;

    private Thread mUIThread;
    private GameBackground mFrameBackground;
    private Dimension mFrameDimension;
    private ArrayList<KoalaGameWall> mKoalaGameWallList = new ArrayList<>();
    private ArrayList<Saw> mSawList = new ArrayList<>();
    private ArrayList<TNT> mTNT = new ArrayList<>();
    private ArrayList<Koala> mKoala = new ArrayList<>();

    private BufferedImage mMapImage;
    private BufferedImage mDisplayImage;

    private Graphics2D mGraphics2d;
    private Graphics2D mDisplayGraphics;
    private GameKeyboardPanel mKoalaGameKeyboardPanel;
    private GameSounds mMusic;
    private GameSounds mFinalSound;
    private Exit mExit;
    private Switch mSwitch;
    private Lock mLock;
    private Rock mRock;
    private Image mGameOverImage;
    private Image mCongratulationImage;
    private Image mRescuedLabel;
    private Sprite mRescuedKoala;
    private Sprite mExplosion;
    private int mCurrentExplosionFrame;
    private boolean mFinishTheGame = false;
    private boolean mGameIsOver = false;

    private Koala mKoalaOne;
    private Koala mKoalaTwo;
    private Koala mKoalaThree;

    public static KoalaGameWorld getInstance() {
        if (sGameWorld == null) {
            sGameWorld = new KoalaGameWorld();
        }
        return sGameWorld;
    }

    private KoalaGameWorld() {
        loadMapObjects();
    }

    private void loadMapObjects() {
        try {
            mFrameBackground = new GameBackground(BACKGROUND_IMAGE_URI);
            mMusic = new GameSounds(BACKGROUND_MUSIC_URI);
            mGameOverImage = ImageIO.read(new File(GAME_OVER_LABEL_URI));
            mCongratulationImage = ImageIO.read(new File(CONGRATS__LABEL_URI));
            mFinalSound = new GameSounds((FINAL_SOUND_URI));
            mRescuedLabel = ImageIO.read(new File(RESCUED_LABEL_URI));
            mRescuedKoala = new Sprite(RESC_KOALA_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
            mExplosion = new Sprite(EXPLOSION_IMAGE_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mFrameDimension = new Dimension(MAP_WIDTH, MAP_HEIGHT);
        loadLevelMap();
    }

    @Override
    public void init() {
        super.init();
        mKoalaGameKeyboardPanel = new GameKeyboardPanel(this, FRAME_WIDTH, FRAME_HEIGHT);
        mKoalaGameKeyboardPanel.setTitle("Koala Game");
        mKoalaGameKeyboardPanel.addWindowListener(this);
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
                    if (mLevelMap[i][j] == WALL_REGULAR) {
                        mKoalaGameWallList.add(new KoalaGameWall(this, WALL_REGULAR_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                    } else if (mLevelMap[i][j] == LOCK_POSITION) {
                        mLock = new Lock(this, LOCK_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i);
                    } else if (mLevelMap[i][j] == SWITCH_POSITION) {
                        mSwitch = new Switch(this, SWITCH_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i);
                    } else if (mLevelMap[i][j] == SAW_RIGHT_POSITION) {
                        mSawList.add(new Saw(this, SAW_RIGHT_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                    } else if (mLevelMap[i][j] == SAW_LEFT_POSITION) {
                        mSawList.add(new Saw(this, SAW_LEFT_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                    }else if (mLevelMap[i][j] == KOALA_ONE_START_POSITION) {
                        mKoala.add(new Koala(this, KOALA_STAND_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                        mKoalaOne = mKoala.get(0);
                    } else if (mLevelMap[i][j] == KOALA_TWO_START_POSITION) {
                        mKoala.add(new Koala(this, KOALA_STAND_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                        mKoalaTwo = mKoala.get(1);
                    } else if (mLevelMap[i][j] == KOALA_THREE_START_POSITION) {
                        mKoala.add(new Koala(this, KOALA_STAND_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                        mKoalaThree = mKoala.get(2);
                    } else if (mLevelMap[i][j] == TNT_POSITION) {
                        mTNT.add(new TNT(this, TNT_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i));
                    } else if (mLevelMap[i][j] == BLUE_EXIT_POSITION) {
                        mExit = new Exit(this, EXIT_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i);
                    } else if (mLevelMap[i][j] == ROCK_POSITION) {
                        mRock = new Rock(this, ROCK_URI, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * j, Sprite.SPRITE_TILE_SIZE_MEDIUM_LARGE * i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initObservers() {
        GameKeyEvent mKoalaOneKeyEvent = new GameKeyEvent();
        GameKeyEvent mKoalaTwoKeyEvent = new GameKeyEvent();
        GameKeyEvent mKoalaThreeKeyEvent = new GameKeyEvent();

        mKoalaOneKeyEvent.addObserver(mKoalaOne);
        mKoalaTwoKeyEvent.addObserver(mKoalaTwo);
        mKoalaThreeKeyEvent.addObserver(mKoalaThree);

        GameKeyAdapter mKoalaOneAdapter = new GameKeyAdapter(mKoalaOneKeyEvent);
        GameKeyAdapter mKoalaTwoAdapter = new GameKeyAdapter(mKoalaTwoKeyEvent);
        GameKeyAdapter mKoalaThreeAdapter = new GameKeyAdapter(mKoalaThreeKeyEvent);

        List<GameKeyAdapter> adapters = new ArrayList<>();
        adapters.add(mKoalaOneAdapter);
        adapters.add(mKoalaTwoAdapter);
        adapters.add(mKoalaThreeAdapter);
        mKoalaGameKeyboardPanel.setKeyAdapters(adapters);
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

            mKoalaOne.setInitialSpriteFrame(mGraphics2d, (mKoalaOne.getSprite().frameCount() / 8) - 1);
            mKoalaTwo.setInitialSpriteFrame(mGraphics2d, (mKoalaTwo.getSprite().frameCount() / 8) - 1);
            mKoalaThree.setInitialSpriteFrame(mGraphics2d, (mKoalaThree.getSprite().frameCount() / 8) - 1);
        }

        updateFrame();
        graphics.drawImage(mDisplayImage, 0, 0, this);
    }

    public void updateFrame() {
        if (!mGameIsOver) {
            Font stringFont = new Font("SansSerif", Font.PLAIN, 18);
            mDisplayGraphics.setFont(stringFont);
            mDisplayGraphics.setColor(Color.white);

            if (Koala.sKoalaCount == 3) {
                setGameToFinish();
            }

            if (!mKoalaOne.isDead() && !mKoalaTwo.isDead() && !mKoalaThree.isDead() && Koala.sKoalaCount != 3 || !mFinishTheGame) {
                renderMap();
                drawWalls();
                drawSaw();
                drawTNT();
                drawExit();
                drawRock();
                drawKoala();
                drawLock();
                drawSwitch();

                mDisplayGraphics.drawImage(mMapImage, 0, 0, this);
                drawRescuedKoalas();
                drawLabels();
            } else if (Koala.sKoalaCount == 3) {
                mGameIsOver = true;
                mFinalSound.playSound();
                mDisplayGraphics.drawImage(mCongratulationImage, FRAME_WIDTH / 3, FRAME_HEIGHT / 4, this);
            } else {
                mGameIsOver = true;
                mDisplayGraphics.drawImage(mGameOverImage, FRAME_WIDTH / 3 + 50, FRAME_HEIGHT / 4, this);
            }
        }
    }


    public void setGameToFinish() {
        mFinishTheGame = true;
    }

    private void drawRescuedKoalas() {
        mDisplayGraphics.drawImage(mRescuedLabel, 0, 520, this);
        for (int index = 0, yPosition = 140; index < Koala.sKoalaCount; index++, yPosition += 60) {
            mDisplayGraphics.drawImage(mRescuedKoala.getFrame(0), yPosition, 520, this);
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

    private void drawKoala() {
        if (mKoalaOne != null && !mKoalaOne.isRemoved()) {
            mKoalaOne.draw(mGraphics2d, this);
        }
        if (mKoalaTwo != null && !mKoalaTwo.isRemoved()) {
            mKoalaTwo.draw(mGraphics2d, this);
        }
        if (mKoalaThree != null && !mKoalaThree.isRemoved()) {
            mKoalaThree.draw(mGraphics2d, this);
        }
    }

    public void drawExit() {
        if (mExit != null) {
            mExit.draw(mGraphics2d, this);
        }
    }

    public void drawLabels() {
        Font stringFont = new Font("SansSerif", Font.PLAIN, 20);
        mDisplayGraphics.setFont(stringFont);
        mDisplayGraphics.drawString("Press Q to Quit", 800, 550);
    }

    public void drawRock() {
        if (mRock != null) {
            mRock.draw(mGraphics2d, this);
        }
    }

    public void drawSwitch() {
        if (mSwitch != null) {
            mSwitch.draw(mGraphics2d, this);
        }
    }

    public void drawLock() {
        if (mLock != null) {
            mLock.draw(mGraphics2d, this);
        }
    }

    private void drawTNT() {
        for (TNT tnt : mTNT) {
            if (!tnt.getExplosionStatus()) {
                tnt.draw(mGraphics2d, this);
            } else {
                if (mCurrentExplosionFrame < mExplosion.getFrameLength()) {
                    tnt.setImage(mExplosion.getFrame(mCurrentExplosionFrame));
                    ++mCurrentExplosionFrame;
                    tnt.draw(mGraphics2d, this);
                } else {
                    setGameToFinish();
                }
            }
        }
    }

    private void drawWalls() {
        for (KoalaGameWall wall : mKoalaGameWallList) {
            wall.draw(mGraphics2d, this);
        }
    }

    private void drawSaw() {
        for (Saw saw : mSawList) {
            saw.draw(mGraphics2d, this);
        }
    }

    public Koala getKoalaOne() {
        return mKoalaOne;
    }

    public Koala getKoalaTwo() {
        return mKoalaTwo;
    }

    public Koala getKoalaThree() {
        return mKoalaThree;
    }

    public Lock getLock() {
        return mLock;
    }

    public Rock getRock() {
        return mRock;
    }

    @Override
    public Dimension getPreferredSize() {
        return mFrameDimension;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mUIThread != null) {
            mMusic.stopSound();
            mUIThread.interrupt();
            mUIThread = null;
            mKoalaGameKeyboardPanel.dispose();
        }
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