package Services;

import Commands.Command;

public interface BasicService {
    void execute(Command request, Command response);
}
