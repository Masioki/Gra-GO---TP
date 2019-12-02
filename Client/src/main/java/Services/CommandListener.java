package Services;

import Domain.Commands.Command;

/**
 * Interfejs do nasłuchiwania akcji
 */
public interface CommandListener {
    void execute(Command command) throws Exception;
}
