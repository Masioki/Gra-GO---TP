package Commands;

public class GameCommand {

    private GameCommandType commandType;
    private int x;
    private int y;


    public GameCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(GameCommandType commandType) {
        this.commandType = commandType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
