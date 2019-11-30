package Services;

import java.io.IOException;

//Wzorzec observer
/**
 * Interfejs źródła danych, które można nasłuchiwać innymi obiektami z interfejsem ResponseListener.
 * @see ResponseListener
 */
public interface EventSource {
    void addListener(ResponseListener listener);

    void waitForEvent() throws IOException, ClassNotFoundException;
}
