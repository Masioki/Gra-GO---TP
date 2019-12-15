package Services;

import Commands.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayerServiceInvoker {

    private InvokableService service;
    private LinkedList<Command> requests;
    private LinkedList<Command> responses;
    private List<CommandListener> listeners;

    public PlayerServiceInvoker(InvokableService service) {
        this.service = service;
        requests = new LinkedList<>();
        responses = new LinkedList<>();
        listeners = new ArrayList<>();
    }


    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public synchronized void send(Command response) {
        if (response != null) responses.add(response);
        Iterator<Command> res = responses.iterator();
        while (res.hasNext()) {
            boolean sended = false;
            Command tempRes = res.next();
            Iterator<Command> req = requests.iterator();
            while (req.hasNext()) {
                Command tempReq = req.next();
                if (tempReq.getUuid() == tempRes.getUuid()) {
                    for (CommandListener l : listeners) l.execute(tempReq, tempRes);
                    req.remove();
                    res.remove();
                    sended = true;
                    break;
                }
            }
            if (!sended) for (CommandListener l : listeners) {
                l.execute(null, tempRes);
                res.remove();
            }
        }
    }

    public void signalEnd() {
        for (CommandListener l : listeners) l.endListening();
    }

    public synchronized void execute(Command command) {
        if (command == null) return;
        requests.add(command);
        send(service.execute(command));
    }

}
