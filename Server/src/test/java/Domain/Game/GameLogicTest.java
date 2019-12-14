package Domain.Game;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicTest {
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

    @Test
    public void standardSuicide() throws Exception
    {
        GameLogic gameLogic = new GameLogic(10);
        gameLogic.placePawn(2,3, false);
        gameLogic.placePawn(2,1, false);
        gameLogic.placePawn(1,2, false);
        gameLogic.placePawn(3,2, false);
        //prubujemy postawic pionka na środku
        boolean placePawn = gameLogic.placePawn(2,2, true);
        assertEquals(placePawn, false);
    }

    @Test
    public void specialSuicide() throws Exception
    {
        GameLogic gameLogic = new GameLogic(19);
        gameLogic.placePawn(1, 0, true);
        gameLogic.placePawn(0, 1, true);
        gameLogic.placePawn(0,2,true);
        gameLogic.placePawn(1,3,true);
        gameLogic.placePawn(2,3,true);
        gameLogic.placePawn(3,2,true);

        //czarne pionki
        gameLogic.placePawn(1,1,false);
        gameLogic.placePawn(1,2,false);
        gameLogic.placePawn(2,2,false);

        boolean outcome = gameLogic.placePawn(2,1,true);
        System.out.println(gameLogic.getBoard().get(new Point(1,1)) );
        System.out.println(gameLogic.getBoard().get(new Point(2,1)) );
        assertEquals(outcome, true);

    }
}
