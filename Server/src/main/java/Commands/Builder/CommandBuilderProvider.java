package Commands.Builder;

// wzorzec Builder i Command
public class CommandBuilderProvider {


    public static SimpleCommandBuilder newSimpleCommandBuilder() {
        return new SimpleCommandBuilder();
    }

    public static GameCommandBuilder newGameCommandBuilder() {
        return new GameCommandBuilder();
    }


}
