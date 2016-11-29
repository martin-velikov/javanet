import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerMain {

    private static final int PORT = 9876;
    public static HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();
    public static Connection connection;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioe) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat?autoReconnect=true&useSSL=false", "root", "admin");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nNew client accepted!");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
