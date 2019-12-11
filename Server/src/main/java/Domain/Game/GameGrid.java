package Domain.Game;

class GameGrid {
    //TODO - angielska nazwa..
    private int breathsNumber;

    public int getBreathsNumber() {
        return breathsNumber;
    }

    public void setBreathsNumber(int breathsNumber) {
        this.breathsNumber = breathsNumber;
    }


    GameGrid() {
        breathsNumber = 0;
    }
}
