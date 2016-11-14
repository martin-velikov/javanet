import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    private static InetAddress host;
    private static final int PORT = 9876;
    private static Socket socket = null;

    public static void main(String[] args) {
        try{
            host = InetAddress.getLocalHost();
        }catch (UnknownHostException uhe){
            System.out.println("\nUnable to find host ID");
            System.exit(1);
        }

        try{
            socket = new Socket(host, PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Scanner userInput = new Scanner(System.in);

            System.out.println(ois.readObject()); //Please enter your username:
            final String username = userInput.nextLine();
            oos.writeObject(username);

            String response;
            do {
                System.out.println(ois.readObject()); //Active users:
                System.out.println(ois.readObject()); // Who do you want to chat with ?
                String recipient = userInput.nextLine();
                oos.writeObject(recipient);
                response = (String) ois.readObject();
            }while(!response.equals("OK"));

           String message;
            do{
                System.out.println("\nEnter message ('QUIT' to exit!): ");
                message = userInput.nextLine();
                TestObject obj = new TestObject(message, username);
                oos.writeObject(obj);
                TestObject received = (TestObject) ois.readObject();
                if(!received.username.equals(obj.username)) {
                    String output = received.username + ": " + received.message;
                    System.out.println(output);
                }
            }while(!message.equals("QUIT"));

            userInput.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                System.out.println("\nClosing connection!");
                socket.close();
            }catch (IOException ioe){
                System.out.println("\nUnable to disconnect!");
                System.exit(1);
            }
        }
    }
}

