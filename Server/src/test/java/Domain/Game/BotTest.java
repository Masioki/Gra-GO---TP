package Domain.Game;

import Domain.Bot;
import Domain.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BotTest {

    @Test
    public void autoMovingTest() {
        DummyGame g = new DummyGame("bot", 19);

        Bot bot = new Bot(19, g);
        g.addPlayer(bot);
        g.end();

    }

    private class DummyGame extends Game {

        public DummyGame(String ownerUsername, int boardSize) {
            super(ownerUsername, boardSize);
        }

        @Override
        public boolean move(int x, int y, Player player) {
            assertTrue(player instanceof Bot);
            return true;
        }

        public void end(){
            endGame = true;
        }
    }
}
