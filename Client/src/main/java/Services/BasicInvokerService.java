package Services;

import Commands.Command;

public interface BasicInvokerService {
    void execute(Command request, Command response);
}
