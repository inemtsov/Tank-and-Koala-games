package tankGame;

import tankGame.game.TankGameWorld;

public class TankGameController {

    public static void main(String[] args) {
        TankGameWorld tankGameWorld = TankGameWorld.getInstance();
        tankGameWorld.init();
        tankGameWorld.start();
    }
}
