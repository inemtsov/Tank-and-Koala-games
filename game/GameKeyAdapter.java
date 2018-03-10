package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter {
    private GameKeyEvent mGameKeyEvent;

    public GameKeyAdapter() {
        super();
    }

    public GameKeyAdapter(GameKeyEvent gameKeyEvent) {
        mGameKeyEvent = gameKeyEvent;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mGameKeyEvent.setKeyValue(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mGameKeyEvent.setKeyValue(e);
    }
}
