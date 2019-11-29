package Services;

import Commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa obsługująca połaczenie z serwerem
 */
public class ServerConnection implements EventSource, ResponseListener {

    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private boolean blockSending;
    private InputListener inputListener;
    private List<ResponseListener> listeners;
    private Socket socket;

    public ServerConnection() throws IOException {
        socket = new Socket("localhost", 10001);
        socket.setSoTimeout(5000);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        outStream.flush();
        inStream = new ObjectInputStream(socket.getInputStream());
        blockSending = false;
        listeners = new ArrayList<>();
        inputListener = new InputListener(inStream);
    }
//TODO: poprawic komunikacje z serwerem

    /**
     * Wysyla komende i czeka na odpowiedz
     *
     * @param command komenda
     * @return odpowiedz od serwera
     * @throws Exception nie wykonano polecenia lub uplynelo 5 sekund
     */
    public synchronized Command sendCommand(Command command) throws Exception {
        if (blockSending) throw new Exception("Poczekaj z ruchem");
        outStream.writeObject(command);
        return (Command) inStream.readObject();
    }

    /**
     * Metoda nasłuchująca
     * @param command komenda od event source
     * @see EventSource
     */
    @Override
    public synchronized void recieve(Command command) {
        blockSending = false;
        for (ResponseListener r : listeners) {
            r.recieve(command);
        }
    }

    /**
     * Metoda blokująca wysyłanie, oczekuje na dane z serwera
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public synchronized void waitForEvent() throws IOException, ClassNotFoundException {
        blockSending = true;
        inputListener.waitForEvent();
    }

    /**
     * Dodaje słuchacza
     * @param listener sluchacz
     * @see ResponseListener
     * @see EventSource
     */
    @Override
    public void addListener(ResponseListener listener) {
        if (listeners != null) listeners.add(listener);
    }

    /**
     * Zakończ połączenie
     * @throws IOException
     */
    public void end() throws IOException {
        inputListener.end();
        socket.close();
    }
}
