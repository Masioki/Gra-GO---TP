import Connection.ClientConnection;
import Services.PlayerService;
import Services.PlayerServiceInvoker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
                    PlayerService service = new PlayerService();
                    PlayerServiceInvoker invoker = new PlayerServiceInvoker(service);
                    service.setClientServiceInvoker(invoker);
                    ClientConnection clientConnection = new ClientConnection(invoker, socket, inStream, outStream);
                    invoker.addListener(clientConnection);
                    clientConnection.start();
                    System.out.println("Nowe polaczenie nr: " + clientConnection.getId());
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