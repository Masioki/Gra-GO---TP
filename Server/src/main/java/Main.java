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
                Socket socket = serverSocket.accept();
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.flush();
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

            }

        } catch (IOException e) {
            System.out.println("blad serwera");
        }
    }
}