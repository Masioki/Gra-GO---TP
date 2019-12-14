package Domain.Game;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicTest {
    //TODO - nie działa
    @Test
    public void killingPawnInCorner() throws Exception
    {
        GameLogic gameLogic = new GameLogic(5);
        gameLogic.placePawn(0,0, true);
        gameLogic.placePawn(0,1, false);
        gameLogic.placePawn(1,0, false);
        assertEquals(gameLogic.getBoard().get(new Point(0,0)), GridState.EMPTY);
    }

    @Test
    public void killingPawnInCenter() throws Exception
    {
        GameLogic gameLogic = new GameLogic(5);
        gameLogic.placePawn(2,2, true);
        gameLogic.placePawn(2,3, false);
        gameLogic.placePawn(2,1, false);
        gameLogic.placePawn(1,2, false);
        gameLogic.placePawn(3,2, false);
        assertEquals(gameLogic.getBoard().get(new Point(2,2)),GridState.EMPTY);
    }
}
