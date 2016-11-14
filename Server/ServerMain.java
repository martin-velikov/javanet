import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static final int PORT = 9876;
    private static HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();

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

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

                oos.writeObject("SERVER> Please enter your username: ");
                String username = (String) ois.readObject();
                System.out.println(username + " connected!\n");

                ClientHandler clientHandler = new ClientHandler(clientSocket, ois, oos);

                clients.put(username, clientHandler);

                String activeUsers = "";
                for(String key : clients.keySet()){
                    activeUsers += key + " ";
                }

                String response = "";
                do {
                    oos.writeObject("\nSERVER> Active users: " + activeUsers + "\n");
                    oos.writeObject("SERVER> Who you want to chat with ?\n");

                    String chatWith = (String) ois.readObject();

                    for (HashMap.Entry<String, ClientHandler> entry : clients.entrySet()) {
                        if (chatWith.equals(entry.getKey())) {
                            clients.get(username).chatWith = entry.getKey();
                            response = "OK";
                        }
                    }
                    oos.writeObject(response);
                }while (!response.equals("OK"));

                clientHandler.start();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void SendObject(TestObject testObj, String chatWith){
        if(!testObj.username.equals(chatWith)){
            clients.get(testObj.username).SendObject(testObj);
        }
        clients.get(chatWith).SendObject(testObj);
    }
}
