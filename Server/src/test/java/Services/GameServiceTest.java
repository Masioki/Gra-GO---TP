package Services;

import DTO.GameData;
import Domain.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService = GameService.getInstance();

    @Test
    public void creatingGameTest() {
        Player player = new Player();
        player.setUsername("Test");
        GameData data = gameService.newGame(19, player, false);
        assertEquals(data.getUsername(), "Test");
    }

    @Test
    public void joiningTest() {
        Player player = new Player();
        player.setUsername("Test");
        GameData data = gameService.newGame(19, player, false);
        Player p1 = new Player();
        Player p2 = new Player();
        p1.setUsername("P1");
        p2.setUsername("P2");

        assertTrue(gameService.joinGame(data, p1));
        assertFalse(gameService.joinGame(data, p2));

    }
}
