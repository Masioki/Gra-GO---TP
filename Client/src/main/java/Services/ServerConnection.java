package Services;

import Commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//TODO: observer
public class ServerConnection {

    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public ServerConnection() throws IOException {
        Socket socket = new Socket("localhost", 10001);
        socket.setSoTimeout(5000);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        outStream.flush();
        inStream = new ObjectInputStream(socket.getInputStream());
    }


    /**
     * Wysyla komende i czeka na odpowiedz
     * @param command komenda
     * @return odpowiedz od serwera
     * @throws Exception nie wykonano polecenia lub uplynelo 5 sekund
     */
    public synchronized Command sendCommand(Command command) throws Exception{
        outStream.writeObject(command);
        return (Command) inStream.readObject();
    }
}
