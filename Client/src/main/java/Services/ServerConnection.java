package Services;

import Commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Klasa obsługująca połaczenie z serwerem
 */
public class ServerConnection implements CommandListener {

    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private boolean end;
    private Socket socket;


    public ServerConnection(ServiceInvoker serviceInvoker) throws IOException {
        socket = new Socket("localhost", 10001);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        outStream.flush();
        inStream = new ObjectInputStream(socket.getInputStream());
        end = false;

        Runnable listener = () -> {
            while (!end) {
                try {
                    if (!socket.getInetAddress().isReachable(2)) end = true;
                    serviceInvoker.execute((Command) inStream.readObject());
                    if (Thread.interrupted()) ServerConnection.this.end();
                } catch (SocketException e) {
                    e.printStackTrace();
                    end = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    @Override
    public synchronized void execute(Command command) throws Exception {
        if (!socket.getInetAddress().isReachable(2)) end = true;
        else outStream.writeObject(command);
    }

    @Override
    public void endListening() {
        end();
    }

    /**
     * Zakończ połączenie
     */
    public void end() {
        end = true;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
