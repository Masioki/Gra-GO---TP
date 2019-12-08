package Domain;

public class GameLogic {
    private int size;
    private Grid [][]grids;
    GameLogic(int size)
    {
        this.size = size;
        grids = new Grid[size][size];
    }

}
