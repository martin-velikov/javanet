import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static final int PORT = 9876;
    private static int clientID = 0;
    private static Socket[] clients;

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioe) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        while (true) {
            System.out.println("\nNew client accepted!");

            clientID++;

            clients[clientID] = serverSocket.accept();

            new ClientHandler(clients[clientID], clientID).start();
        }
    }
}
