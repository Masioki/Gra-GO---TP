package Services;

import Commands.Command;

public interface InvokableService {
    /**
     * @param request
     * @return Command response
     */
    Command execute(Command request);
}
