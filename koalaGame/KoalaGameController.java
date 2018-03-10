package koalaGame;

import koalaGame.game.KoalaGameWorld;

public class KoalaGameController {

    public static void main(String[] args) {
        KoalaGameWorld koalaGameWorld = KoalaGameWorld.getInstance();
        koalaGameWorld.init();
        koalaGameWorld.start();
    }
}
