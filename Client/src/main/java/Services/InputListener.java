package Services;

import Commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa nasłuchująca input.
 * Po odebraniu sygnału wysyła do listenerów
 */
public class InputListener extends Thread implements EventSource {

    private List<ResponseListener> listeners;
    private boolean end;
    private ObjectInputStream inputStream;

    public InputListener(ObjectInputStream inputStream) {
        end = false;
        listeners = new ArrayList<>();
        this.inputStream = inputStream;
    }

    /**
     * Zakończ wątek
     */
    public void end() {
        end = true;
    }

    /**
     * Dodaje słuchacza
     * @param listener słuchacz
     */
    @Override
    public void addListener(ResponseListener listener) {
        if (listeners != null) listeners.add(listener);
    }

    /**
     * Nasłuchuj do uzyskania pierwszego obiektu.
     * Blokuje wątek.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void waitForEvent() throws IOException, ClassNotFoundException {
        Command c = (Command) inputStream.readObject();
        for (ResponseListener r : listeners) {
            r.recieve(c);
        }
    }

    @Override
    public void run() {
        while (!end) {

        }
    }
}
