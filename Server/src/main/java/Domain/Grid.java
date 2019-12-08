package Domain;

public class Grid {
    private GridState gridState;

    public GridState getGridState() {
        return gridState;
    }
    public void setGridState(GridState gridState) {
        this.gridState = gridState;
    }
    Grid()
    {
        gridState = GridState.EMPTY;
    }
}
