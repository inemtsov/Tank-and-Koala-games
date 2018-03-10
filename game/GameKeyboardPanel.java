package game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameKeyboardPanel extends JFrame {
    private Dimension mFrameDimension;

    public GameKeyboardPanel(Component context, int width, int height) {
        super();
        setFocusable(true);
        getContentPane().add("Center", context);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        mFrameDimension = new Dimension(width, height);
        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return mFrameDimension;
    }

    public void setKeyAdapters(List<GameKeyAdapter> adapterList) {
        for(GameKeyAdapter adapter : adapterList) {
            addKeyListener(adapter);
        }
    }
}
