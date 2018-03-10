package game.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    public static final int SPRITE_TILE_SIZE_SMALL = 24;
    public static final int SPRITE_TILE_SIZE_MEDIUM = 32;
    public static final int SPRITE_TILE_SIZE_MEDIUM_LARGE = 40;
    public static final int SPRITE_TILE_SIZE_LARGE = 64;

    private int mTileSize;
    private String mSpriteFile;
    private BufferedImage[] mImages;

    public Sprite(String spriteFile, int tileSize) throws IOException {
        mSpriteFile = spriteFile;
        mTileSize = tileSize;

        loadImages();
    }

    private void loadImages() throws IOException {
        BufferedImage image = ImageIO.read(new File(mSpriteFile));

        mImages = new BufferedImage[image.getWidth() / mTileSize];

        for (int i = 0; i < mImages.length; i++) {
            mImages[i] = image.getSubimage(
                    i * mTileSize, 0, mTileSize, mTileSize
            );
        }
    }

    public BufferedImage getFrame(int frame) {
        return mImages[frame];
    }

    public int getFrameLength() {
        return mImages.length;
    }

    public int frameCount() {
        return mImages.length;
    }

    public int getWidthAtFrame(int frame) {
        if (frame < 0 || frame >= mImages.length){
            return 0;
        } else {
            return mImages[frame].getWidth();
        }
    }
}
