package Services;

import Commands.Command;

public interface InvokableService {
    void execute(Command request, Command response);
}
