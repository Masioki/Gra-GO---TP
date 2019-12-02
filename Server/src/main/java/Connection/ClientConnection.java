package Connection;

import DTO.Commands.Command;
import Services.ConnectionService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Polaczenie z klientem
 */
public class ClientConnection extends Thread {

    private ConnectionService connectionService;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private boolean end;

    public ClientConnection(ObjectInputStream inStream, ObjectOutputStream outStream) {
        this.inStream = inStream;
        this.outStream = outStream;
        end = false;
        connectionService = new ConnectionService(this);

        /*
        Nowy watek do odczytywanie z inStreama, poniewaz jesli nie ma zadnych danych to sie blokuje
        Nie chcemy blokowac calej gry wtedy
         */
        Runnable listener = () -> {
            while (!end) {
                try {
                    sendCommand(connectionService.execute((Command) inStream.readObject()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Thread.interrupted()) return;
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    public void end() {
        end = true;
    }

    public void sendCommand(Command command) {
        try {
            outStream.writeObject(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!end) {
            if (Thread.interrupted()) end();
        }
    }
}
