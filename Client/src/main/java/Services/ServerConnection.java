package Services;

import Commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Klasa obsługująca połaczenie z serwerem
 */
public class ServerConnection implements CommandListener {

    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private boolean end;
    private Socket socket;
    private ServiceInvoker serviceInvoker;


    public ServerConnection(ServiceInvoker serviceInvoker) throws IOException {
        socket = new Socket("localhost", 10001);
        socket.setSoTimeout(5000);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        outStream.flush();
        inStream = new ObjectInputStream(socket.getInputStream());
        end = false;
        this.serviceInvoker = serviceInvoker;

        Runnable listener = () -> {
            while (!end) {
                try {
                    serviceInvoker.execute((Command) inStream.readObject());
                    if (Thread.interrupted()) ServerConnection.this.end();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }


    public synchronized void execute(Command command) throws Exception {
        outStream.writeObject(command);
    }

    /**
     * Zakończ połączenie
     *
     * @throws IOException
     */
    public void end() throws IOException {
        end = true;
        socket.close();
    }
}
