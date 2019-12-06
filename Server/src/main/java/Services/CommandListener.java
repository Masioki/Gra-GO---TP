package Services;

import Commands.Command;

public interface CommandListener {
    void execute(Command request, Command response);
    void endListening();
}
