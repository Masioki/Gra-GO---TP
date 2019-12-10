package Domain;

public class GameGrid {
    //TODO - angielska nazwa..
    private int breathsNumber;
    private boolean checked;

    public int getBreathsNumber() {
        return breathsNumber;
    }

    public void setBreathsNumber(int breathsNumber) {
        this.breathsNumber = breathsNumber;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    GameGrid()
    {
        breathsNumber = 0;
        checked = false;
    }
}
