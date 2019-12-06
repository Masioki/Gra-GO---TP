package Services;

import Commands.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientServiceInvoker {

    private InvokableService service;
    private LinkedList<Command> requests;
    private LinkedList<Command> responses;
    private List<CommandListener> listeners;

    public ClientServiceInvoker(InvokableService service) {
        this.service = service;
        requests = new LinkedList<>();
        responses = new LinkedList<>();
        listeners = new ArrayList<>();
    }


    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public void send(Command response) {
        if (response != null) responses.add(response);
        boolean repeat;
        do {
            repeat = false;
            Iterator<Command> i = responses.iterator();
            while (i.hasNext()) {
                Command c = i.next();
                if (requests.size() != 0 && c.getUuid() == requests.get(0).getUuid()) {
                    for (CommandListener l : listeners) l.execute(requests.get(0), c);
                    requests.removeFirst();
                    i.remove();
                    repeat = true;
                    break;
                }
            }

        } while (repeat);
    }

    public void signalEnd() {
        for (CommandListener l : listeners) l.endListening();
    }

    public void execute(Command command) {
        if (command == null) return;
        requests.add(command);
        send(service.execute(command));
    }

}
