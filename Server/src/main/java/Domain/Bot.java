package Domain;

import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.Game.GameObserver;

public class Bot extends Player implements GameObserver {

    public Bot() {
        username = "bot";
    }

    @Override
    public void action(int x, int y, String username, PawnColor color, GameCommandType type) {

    }
}
