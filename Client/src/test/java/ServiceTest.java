import Commands.Command;
import Commands.CommandType;
import Commands.GameCommandType;
import Services.InvokableService;
import Services.Service;
import Services.ServiceInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void inject() {
        service = Service.getInstance();
        service.setServiceInvoker(new DummyServiceInvoker(service));
    }

    @Test
    public void commandPassingTest() {
        service.signUp("Test", "test");
        service.end();
        service.loadActiveGames();
        service.joinGame(null);
        service.newGame(false);
    }


    private class DummyServiceInvoker extends ServiceInvoker {

        int i = 0;

        public DummyServiceInvoker(InvokableService service) {
            super(service);
        }

        @Override
        public synchronized void send(Command command) throws Exception {
            switch (i) {
                case 0: {
                    assertEquals(command.getType(), CommandType.LOGIN);
                    break;
                }
                case 1: {
                    assertEquals(command.getType(), CommandType.END_CONNECTION);
                    break;
                }
                case 2: {
                    assertEquals(command.getType(), CommandType.ACTIVE_GAMES);
                    break;
                }
                case 3: {
                    assertEquals(command.getType(), CommandType.JOIN);
                    break;
                }
                case 4: {
                    assertEquals(command.getType(), CommandType.NEW);
                    break;
                }
            }
            i++;
        }
    }
}
