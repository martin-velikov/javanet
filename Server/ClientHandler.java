import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler extends Thread{
    private Socket socket;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;

    private static String username;
    private static String chatWith;

    public ClientHandler (Socket socket){
        this.socket = socket;
        try{
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    public void run() {
        try {
            oos.writeObject("SERVER> Please enter your username: ");
            username = (String) ois.readObject();
            System.out.println(username + " connected!\n");

            ServerMain.clients.put(username, this);

            String response = "";
            do {
                String activeUsers = "";
                for(String key : ServerMain.clients.keySet()){
                    activeUsers += key + " ";
                }

                oos.writeObject("\nSERVER> Active users: " + activeUsers + "\n");
                oos.writeObject("SERVER> Who you want to chat with ?\n");

                chatWith = (String) ois.readObject();

                for (HashMap.Entry<String, ClientHandler> entry : ServerMain.clients.entrySet()) {
                    if (chatWith.equals(entry.getKey())) {
                        response = "OK";
                    }
                }
                oos.writeObject(response);
            }while (!response.equals("OK"));


            TestObject testObj;
            do{
                testObj = (TestObject) ois.readObject();
                SendObject(testObj);
            }while (!testObj.message.equals("QUIT"));

        }catch (Exception Ex){
            Ex.printStackTrace();
        }

        try{
            if(socket != null){
                System.out.println("\nClosing down connection!");
                ServerMain.clients.remove(username, this);
                socket.close();
            }
        }catch (IOException ioe){
            System.out.println("\nUnable to disconnect!");
        }
    }

    private static void SendObject(TestObject testObj){
        try {
            if (!testObj.username.equals(chatWith)) {
                ServerMain.clients.get(testObj.username).oos.writeObject(testObj);
            }
            ServerMain.clients.get(chatWith).oos.writeObject(testObj);
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }
}
