import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static final int PORT = 9876;

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioe) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("\nNew client accepted!");

            new ClientHandler(clientSocket).start();
        }
    }
}
