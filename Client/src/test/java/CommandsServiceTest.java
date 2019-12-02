import Domain.Commands.Builder.CommandBuilderProvider;
import Domain.Commands.Builder.GameCommandBuilder;
import Domain.Commands.Builder.SimpleCommandBuilder;
import Domain.Commands.Command;
import Domain.Commands.CommandParser;
import Domain.Commands.CommandType;
import Domain.GameData;
import Domain.LoginData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandsServiceTest {
    private SimpleCommandBuilder simpleCommandBuilder;
    private GameCommandBuilder gameCommandBuilder;
    private CommandParser commandParser;

    @BeforeEach
    public void injectServices() {
        simpleCommandBuilder = CommandBuilderProvider.newSimpleCommandBuilder();
        gameCommandBuilder = CommandBuilderProvider.newGameCommandBuilder();
        commandParser = new CommandParser();
    }

    @Test
    public void commandMapping() throws Exception {
        LoginData l = new LoginData();
        l.setUsername("nazwa");
        l.setPassword("haslo");

        Command c = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(l)
                .build();
        assertEquals(c.getBody(), "{\"username\":\"nazwa\",\"password\":\"haslo\"}");
        assertEquals(CommandType.LOGIN, c.getType());
    }

    @Test
    public void commandParsing() throws Exception {
        List<GameData> list = new ArrayList<>();
        GameData g1 = new GameData();
        GameData g2 = new GameData();
        g1.setGameID(1);
        g1.setUsername("pierwszy");
        g2.setGameID(2);
        g2.setUsername("drugi");
        list.add(g1);
        list.add(g2);
        Command c = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.SUCCESS)
                .withBody(list)
                .build();
        List<GameData> result = commandParser.parseActiveGamesCommand(c.getBody());

        assertEquals(g1.getGameID(), result.get(0).getGameID());
        assertEquals(g2.getGameID(), result.get(1).getGameID());
        assertEquals(g1.getUsername(), result.get(0).getUsername());
        assertEquals(g2.getUsername(), result.get(1).getUsername());
    }
}
