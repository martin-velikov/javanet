import java.io.IOException;
import java.io.PrintWriter;
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
            Scanner input = new Scanner(socket.getInputStream());
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner userInput = new Scanner(System.in);

            String message ="", response = "";

            do{
                System.out.println("\nEnter message ('QUIT' to exit!): ");
                message = userInput.nextLine();
                output.println(message);
                response = input.nextLine();
                System.out.println("SERVER> " + response);
            }while(!message.equals("QUIT"));

            input.close();
            userInput.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
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
