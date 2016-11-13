import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static final int PORT = 9876;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioe) {
            System.out.println("\nUnable to set up port!");
            System.exit(2);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("\nNew client accepted!");
            }catch (IOException ioe){
                System.out.println("\nCannot assign socket!");
            }
            new ClientHandler(clientSocket).start();
        }
    }
}
