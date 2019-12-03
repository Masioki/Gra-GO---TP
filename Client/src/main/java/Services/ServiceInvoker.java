package Services;

import Commands.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServiceInvoker {

    private BasicService service;
    private ServerConnection serverConnection;
    private List<Command> sended;
    private List<Command> received;
    private List<CommandListener> listeners;

    public ServiceInvoker(BasicService service) {
        this.service = service;
        sended = new LinkedList<>();
        received = new LinkedList<>();
        listeners = new ArrayList<>();
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public void send(Command command) throws Exception {
        for (CommandListener l : listeners) l.execute(command);
        sended.add(command);
    }

    public void execute(Command command) {
        received.add(command);
        boolean repeat;
        do {
            repeat = false;
            Iterator<Command> i = received.iterator();
            while (i.hasNext()) {
                Command c = i.next();
                if (sended.size() != 0 && c.getUuid() == sended.get(0).getUuid()) {
                    service.execute(sended.get(0), c);
                    sended.remove(0);
                    i.remove();
                    repeat = true;
                    break;
                }
            }

        } while (repeat);
    }

}
