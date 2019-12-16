package Domain.Game;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Map;

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
    public void killingPawnsInCenter() throws Exception
    {
        GameLogic gameLogic = new GameLogic(5);
        gameLogic.placePawn(2,2, true);
        gameLogic.placePawn(2,3, false);
        gameLogic.placePawn(2,1, false);
        gameLogic.placePawn(1,2, false);
        gameLogic.placePawn(3,2, false);
        assertEquals(GridState.EMPTY,gameLogic.getBoard().get(new Point(2,2)));
        gameLogic.placePawn(2,4,true);
        gameLogic.placePawn(3,4,false);
        gameLogic.placePawn(1,4,false);
        assertEquals(GridState.EMPTY,gameLogic.getBoard().get(new Point(2,4)));
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
        GameLogic gameLogic = new GameLogic(5);
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
        gameLogic.placePawn(2,0,false);
        gameLogic.placePawn(3,1,false);

        boolean outcome = gameLogic.placePawn(2,1,true);
        System.out.println(gameLogic.getBoard().get(new Point(1,1)) );
        System.out.println(gameLogic.getBoard().get(new Point(2,1)) );
        assertEquals(true, outcome);

    }

    @Test
    public void scoreTest()
    {
        GameLogic gameLogic = new GameLogic(6);
        assertEquals(0, gameLogic.getFinalScore(false));
        //białe
        gameLogic.placePawn(3,3,true);
        gameLogic.placePawn(4,3,true);
        gameLogic.placePawn(5,3,true);
        gameLogic.placePawn(4,4,true);
        gameLogic.placePawn(4,5, true);
        gameLogic.placePawn(3,5, true);
        //czarne
        gameLogic.placePawn(3,4, false);

        assertEquals(8, gameLogic.getFinalScore(true));
    }

    @Test
    public void colorTest()
    {
        GameLogic gameLogic = new GameLogic(6);
        gameLogic.placePawn(3,3,true);
        gameLogic.placePawn(3,3,false);
        assertEquals(GridState.WHITE, gameLogic.getBoard().get(new Point(3,3)));
    }

    @Test
    public void poindInsideBoardTest()
    {
        GameLogic gameLogic = new GameLogic(6);
        assertEquals(true, gameLogic.checkIfPointInsideBoard(5,5));
        assertEquals(false, gameLogic.checkIfPointInsideBoard(5,6));
        assertEquals(false, gameLogic.checkIfPointInsideBoard(-1,3));
    }

    @Test
    public void getPlayerTemporaryScore()
    {
        GameLogic gameLogic = new GameLogic(6);
        gameLogic.placePawn(3,3,true);

        gameLogic.placePawn(3,4,false);
        gameLogic.placePawn(3,2,false);
        gameLogic.placePawn(2,3,false);
        gameLogic.placePawn(4,3,false);

        assertEquals(1,gameLogic.getPlayerPoints(false));
        assertEquals(0,gameLogic.getPlayerPoints(true));
    }

    @Test
    public void breathsSimpleTest()
    {
        GameGrid gameGrid = new GameGrid();
        gameGrid.setBreathsNumber(3);
        assertEquals(3,gameGrid.getBreathsNumber());
    }
}
