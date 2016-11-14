import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public String chatWith;

    public ClientHandler (Socket socket, ObjectInputStream ois, ObjectOutputStream oos){
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    public void run() {
        try {
            TestObject to;

            do{
                to = (TestObject) ois.readObject();
                ServerMain.SendObject(to, chatWith);
            }while (!to.message.equals("QUIT"));

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

    public void SendObject(TestObject testObj){
        try{
            oos.writeObject(testObj);
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }
}
