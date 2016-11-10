import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread{
    private Socket socket;
    private Scanner input;
    private PrintWriter output;

    public ClientHandler (Socket socket){
        this.socket = socket;

        try {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void run() {

        String received = input.nextLine();

        while(!received.equals("QUIT")){
            output.println("ECHO: " + received);
            received = input.nextLine();
        }

        try{
            if(socket != null){
                System.out.println("\nClosing down connection!");
                socket.close();
            }
        }catch (IOException ioe){
            System.out.println("\nUnable to disconnect!");
        }
    }
}
