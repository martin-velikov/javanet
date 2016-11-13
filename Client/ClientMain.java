import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientMain {

    private static InetAddress host;
    private static final int PORT = 9876;
    private static Socket socket = null;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        try{
            host = InetAddress.getByName("192.168.0.108");
        }catch (UnknownHostException uhe){
            System.out.println("\nUnable to find host ID");
            System.exit(1);
        }

        try{
            socket = new Socket(host, PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            Scanner userInput = new Scanner(System.in);

            String message ="";

            do{
                System.out.println("\nEnter message ('QUIT' to exit!): ");
                message = userInput.nextLine();
                TestObject obj = new TestObject(message);
                oos.writeObject(obj);
                TestObject received = (TestObject) ois.readObject();
                System.out.println(received.message);
            }
            while(!message.equals("QUIT"));
            userInput.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
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

