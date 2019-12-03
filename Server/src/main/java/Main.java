import Connection.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Main {
    public static void main(String[] args) {
        //ServerSocket implementuje AutoClosable
        try (ServerSocket serverSocket = new ServerSocket(10001)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                    outStream.flush();
                    ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                    ClientConnection clientConnection = new ClientConnection(socket,inStream, outStream);
                    clientConnection.start();
                    System.out.println("Nowe polaczenie");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}