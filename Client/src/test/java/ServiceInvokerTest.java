import Commands.Builder.CommandBuilderProvider;
import Commands.Builder.SimpleCommandBuilder;
import Commands.Command;
import Commands.CommandType;
import Services.InvokableService;
import Services.ServiceInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceInvokerTest {
    private ServiceInvoker serviceInvoker;
    private InvokableService service;
    private SimpleCommandBuilder simpleCommandBuilder;

    @BeforeEach
    public void inject() {
        simpleCommandBuilder = CommandBuilderProvider.newSimpleCommandBuilder();
        service = new DummyInvokerService();
        serviceInvoker = new ServiceInvoker(service);
    }

    @Test
    public void invokerTest() throws Exception {
        Command request1 = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.JOIN)
                .build();
        Command response1 = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.SUCCESS)
                .build();
        response1.setUuid(request1.getUuid());
        Command request2 = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .build();
        Command response2 = simpleCommandBuilder
                .newCommand()
                .withHeader(CommandType.ERROR)
                .build();
        response2.setUuid(request2.getUuid());

        //Podajemy w nieoczywistej kolejnosci
        serviceInvoker.send(request2);
        serviceInvoker.send(request1);
        serviceInvoker.execute(response1);
        serviceInvoker.execute(response2);
        //oczekujemy najpierw LOGIN ERROR a potem JOIN SUCCESS
    }

    private class DummyInvokerService implements InvokableService {
        private int i = 0;
        @Override
        public void execute(Command request, Command response) {
            System.out.println(request.getType());
            System.out.println(response.getType());
            if(i==0){
                assertEquals(request.getType(),CommandType.LOGIN);
                assertEquals(response.getType(),CommandType.ERROR);
                i++;
            } else {
                assertEquals(request.getType(),CommandType.JOIN);
                assertEquals(response.getType(),CommandType.SUCCESS);
            }
        }
    }
}
