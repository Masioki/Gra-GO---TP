package Connection;

import DTO.Commands.Command;
import Services.ConnectionService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Polaczenie z klientem
 */
public class ClientConnection extends Thread {

    private ConnectionService connectionService;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private boolean end;

    public ClientConnection(Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        this.inStream = inStream;
        this.outStream = outStream;
        this.socket = socket;
        end = false;
        connectionService = new ConnectionService(this);

        /*
        Nowy watek do odczytywanie z inStreama, poniewaz jesli nie ma zadnych danych to sie blokuje
        Nie chcemy blokowac calej gry wtedy
         */
        Runnable listener = () -> {
            while (!end) {
                try {
                    if (!socket.getInetAddress().isReachable(2)) end = true;
                    sendCommand(connectionService.execute((Command) inStream.readObject()));
                } catch (SocketException e) {
                    e.printStackTrace();
                    end = true;
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (Thread.interrupted()) return;
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    public void end() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = true;
    }

    public void sendCommand(Command command) {
        try {
            if (!socket.getInetAddress().isReachable(2)) end = true;
            else outStream.writeObject(command);
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
