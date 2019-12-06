package Connection;

import Commands.Command;
import Services.ClientServiceInvoker;
import Services.CommandListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Polaczenie z klientem
 */
public class ClientConnection extends Thread implements CommandListener {

    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private ClientServiceInvoker invoker;
    private boolean end;

    public ClientConnection(ClientServiceInvoker invoker, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        this.inStream = inStream;
        this.outStream = outStream;
        this.socket = socket;
        end = false;
        this.invoker = invoker;

        /*
        Nowy watek do odczytywanie z inStreama, poniewaz jesli nie ma zadnych danych to sie blokuje
        Nie chcemy blokowac calej gry wtedy
         */
        Runnable listener = () -> {
            while (!end) {
                try {
                    if (!socket.getInetAddress().isReachable(2)) end = true;
                    invoker.execute((Command) inStream.readObject());
                } catch (SocketException e) {
                    e.printStackTrace();
                    System.out.println("koniec");
                    end = true;
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
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = true;
    }


    @Override
    public void run() {
        while (!end) {
            if (Thread.interrupted()) end();
        }
    }

    @Override
    public void execute(Command request, Command response) {
        try {
            if (!socket.getInetAddress().isReachable(2)) end = true;
            else outStream.writeObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endListening() {
        end();
    }
}
