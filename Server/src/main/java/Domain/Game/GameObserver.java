package Domain.Game;

import Commands.GameCommandType;

public interface GameObserver {
    void action(int x, int y, String username, GameCommandType type);

}
