package Services;

import Commands.Builder.CommandBuilderProvider;
import Commands.Command;
import Commands.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InvokerTest {

    private PlayerServiceInvoker invoker;

    @BeforeEach
    public void inject() {
        invoker = new PlayerServiceInvoker(request -> {
            System.out.println(request.getType());
            return CommandBuilderProvider
                    .newSimpleCommandBuilder()
                    .newCommand()
                    .withHeader(CommandType.SUCCESS)
                    .withUUID(request.getUuid())
                    .build();
        });
    }

    @Test
    public void commandPairingTest() {
        Command request = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .build();
        Command falseRequest = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.NEW)
                .build();

        invoker.addListener(new CommandListener() {
            int i = 0;

            @Override
            public void execute(Command req, Command res) {
                if (i != 0) assertEquals(req.getUuid(), res.getUuid());
                else {
                    assertNull(req);
                    i++;
                }
            }

            @Override
            public void endListening() {

            }
        });

        invoker.send(falseRequest);
        invoker.execute(request);

    }
}
