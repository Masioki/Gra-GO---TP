package Commands;

import Commands.Builder.CommandBuilderProvider;
import Commands.Builder.GameCommandBuilder;
import Commands.Builder.SimpleCommandBuilder;
import DTO.LoginData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandServiceTest {
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
    public void commandParsing() throws Exception{
        LoginData l = new LoginData();
        l.setUsername("nazwa");
        l.setPassword("haslo");
        Command c = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(l)
                .build();
        LoginData result = commandParser.parseLoginCommand(c.getBody());

        assertEquals(result.getPassword(),l.getPassword());
        assertEquals(result.getUsername(),l.getUsername());
    }
}
