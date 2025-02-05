package Domain.Game;

import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.Player;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void constructorTest()
    {
        Game game = new Game("Tester", 5);
        assertNotNull(game.getGameID());
        assertNotNull(game.getBoard());
        assertEquals(5,game.getSize());
        assertEquals("Tester",game.getOwnerUsername());
    }
    @Test
    public void playersTest()
    {
        Game game = new Game("Tester1", 5);
        Player player1 = new Player();
        player1.setUsername("Tester1");
        game.addPlayer(player1);
        Player player2 = new Player();
        player2.setUsername("Tester2");
        game.addPlayer(player2);
        game.addObserver((x, y, username, color, type) -> {

        });
        assertTrue(game.isPlayerTurn(player1));
        //prubujemy dodać gracza 3
        assertFalse(game.addPlayer(player1));
        assertEquals(0, game.getOwnScore("Tester1"));
        assertEquals(0, game.getOpponentScore("Tester2"));
        assertTrue(game.inProgress());
        game.move(3,3,player1);
        assertFalse(game.isPlayerTurn(player1));
        assertFalse(game.move(3, 3, player1));
        assertTrue(game.move(4, 3, player2));
        assertTrue(game.move(3, 4, player1));
        assertNotEquals(game.getBoard().get(new Point(4, 3)), PawnColor.WHITE);
        assertFalse(game.pass(player1));
        assertTrue(game.pass(player2));
        assertTrue(game.pass(player1));
    }
    @Test
    public void scoreTest()
    {
        Game game = new Game("Tester1", 5);
        Player player1 = new Player();
        player1.setUsername("Tester1");
        game.addPlayer(player1);
        Player player2 = new Player();
        player2.setUsername("Tester2");
        game.addPlayer(player2);
        game.addObserver((x, y, username, color, type) -> {

        });
        game.pass(player1);
        game.move(3,4,player2);
        game.move(3,3,player1);
        game.move(3,2,player2);
        game.move(4,4,player1);
        game.move(4,3,player2);
        game.move(0,0,player1);
        game.move(2,3,player2);
        assertEquals(2,game.getOwnScore("Tester2"));
        System.out.println(game.pass(player1) );
        System.out.println(game.pass(player2) );
    }
    @Test
    public void surrenderTest()
    {
        Game game = new Game("Tester1", 5);
        Player player1 = new Player();
        player1.setUsername("Tester1");
        game.addPlayer(player1);
        Player player2 = new Player();
        player2.setUsername("Tester2");
        game.addPlayer(player2);
        game.addObserver(new GameObserver() {
            @Override
            public void action(int x, int y, String username, PawnColor color, GameCommandType type) {

            }
        });
        game.surrender(player1);
        assertFalse(game.inProgress());
    }
}
