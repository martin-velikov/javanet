import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientHandler (Socket socket){
        this.socket = socket;

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void run() {
        try {
           TestObject to = (TestObject) ois.readObject();

           while (!to.message.equals("QUIT")) {
               System.out.println(to.message);
               oos.writeObject(to);
               to = (TestObject) ois.readObject();
           }
        }catch (Exception Ex){
            Ex.printStackTrace();
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
