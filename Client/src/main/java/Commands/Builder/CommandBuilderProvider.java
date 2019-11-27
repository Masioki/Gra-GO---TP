package Commands.Builder;


public class CommandBuilderProvider {


    public static SimpleCommandBuilder newSimpleCommandBuilder() {
        return new SimpleCommandBuilder();
    }

    public static GameCommandBuilder newGameCommandBuilder() {
        return new GameCommandBuilder();
    }


}
