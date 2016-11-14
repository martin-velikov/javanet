import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static final int PORT = 9876;
    public static HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioe) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nNew client accepted!");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
