import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private Socket socket;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;

    public ClientHandler (Socket socket){
        this.socket = socket;
        try{
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    public void run() {
        LoginData loginData = null;

        try {
            String response = "";
            do {
                loginData = (LoginData) ois.readObject();
//                if(loginData.username.equals("lqlq")){
//                    response = "OK";
//                }
                response = "OK"; //==============TEST=============//
                oos.writeObject(response);
            }while(!response.equals("OK"));
            System.out.println(loginData.username + " connected!\n");

            ServerMain.clients.put(loginData.username, this);

            ArrayList<String> clients = new ArrayList<String>();
            for(String name : ServerMain.clients.keySet()){
                clients.add(name);
            }
            oos.writeObject(clients);

            while(true){
                String message = loginData.username + ": " + ois.readObject();

                for(ClientHandler clientHandler : ServerMain.clients.values()){
                    clientHandler.oos.writeObject(message);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(socket != null){
                System.out.println("\n" + loginData.username + " disconnected!\n");
                ServerMain.clients.remove(loginData.username, this);
                socket.close();
            }
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }
}
