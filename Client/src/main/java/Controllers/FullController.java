package Controllers;

import Commands.GameCommandType;
import Domain.GameData;

import java.util.List;

public abstract class FullController {

    public abstract void error(String message);

    public void logIn(boolean success) {

    }


    public void loadActiveGames(List<GameData> games) {

    }

    public void joinGame(GameData data) {

    }

    public void move(int x, int y, boolean me) {

    }

    public void gameAction(GameCommandType actionType, boolean me) {

    }

}
