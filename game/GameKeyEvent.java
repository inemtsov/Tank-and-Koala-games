package game;

import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameKeyEvent extends Observable {
    private int mKey;
    private int mId;

    public void setKeyValue(KeyEvent event) {
        mKey = event.getKeyCode();
        mId = event.getID();
        setChanged();
        notifyObservers(this);
    }

    public int getKey() {
        return mKey;
    }

    public int getId() {
        return mId;
    }
}
