package Services;

import Commands.*;
import Commands.Builder.CommandBuilderProvider;
import DTO.GameData;
import DTO.LoginData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    private PlayerService playerService;


    @BeforeEach
    public void setUp() {
        playerService = new PlayerService();
    }

    @Test
    public void commandExecutingTest() {
        LoginData b = new LoginData();
        LoginData fir = new LoginData();
        LoginData sec = new LoginData();
        b.setUsername("bot");
        b.setPassword("oss");
        fir.setUsername("playertest");
        fir.setPassword("osss");
        sec.setUsername("playertest");
        sec.setPassword("ssss");

        Command bot = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(b)
                .build();
        Command f = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(fir)
                .build();
        Command s = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(sec)
                .build();

        assertEquals(CommandType.ERROR, playerService.execute(bot).getType());
        assertEquals(CommandType.SUCCESS, playerService.execute(f).getType());
        assertEquals(CommandType.ERROR, playerService.execute(s).getType());
    }

    @Test
    public void gameCommandExecutingTest() throws Exception {
        Command game = CommandBuilderProvider
                .newGameCommandBuilder()
                .newCommand()
                .withHeader(GameCommandType.MOVE)
                .build();

        assertEquals(CommandType.ERROR, playerService.execute(game).getType());
    }
}
