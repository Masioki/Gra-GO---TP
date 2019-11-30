package Services;

import Commands.Command;

/**
 * Interfejs do nasłuchiwania akcji
 * @see EventSource
 */
public interface ResponseListener {
    void recieve(Command command);
}
