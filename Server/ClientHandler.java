import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    private Socket socket;
    private String myUsername = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                Object receivedObject = ois.readObject();

                if (receivedObject.getClass() == ObjLoginData.class) {
                    LoginDataReceived((ObjLoginData) receivedObject);
                } else if (receivedObject.getClass() == ObjGroupChatMessage.class) {
                    GroupChatMessageReceived((ObjGroupChatMessage) receivedObject);
                } else if (receivedObject.getClass() == ObjOpenPrivateChat.class) {
                    OpenPrivateChatReceived((ObjOpenPrivateChat) receivedObject);
                } else if (receivedObject.getClass() == ObjPrivateChatMessage.class) {
                    PrivateChatMessageReceived((ObjPrivateChatMessage) receivedObject);
                } else if (receivedObject.getClass() == ObjRegisterData.class) {
                    RegisterDatReceived((ObjRegisterData) receivedObject);
                } else if (receivedObject.getClass() == ObjClosePrivateChat.class) {
                    ClosePrivateChatReceived((ObjClosePrivateChat) receivedObject);
                }
            }
        } catch (Exception e) {
            System.out.println(myUsername + " disconnected!\n");
        }

        try {
            if (socket != null) {
                ServerMain.clients.remove(myUsername);
                RefreshClients();
                socket.close();
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private void ClosePrivateChatReceived(ObjClosePrivateChat objClosePrivateChat) throws IOException {
        ServerMain.clients.get(objClosePrivateChat.receiver).oos.writeObject(objClosePrivateChat);
    }

    private void RegisterDatReceived(ObjRegisterData objRegisterData) throws SQLException, IOException {
        String response = "";

        ResultSet rs = ServerMain.connection.createStatement().executeQuery("SELECT * FROM users WHERE username = '" + objRegisterData.username + "'");
        if (rs.next()) {
            response = "TAKEN";
        } else {
            ServerMain.connection.createStatement().executeUpdate("insert into users(username, pass) values('" + objRegisterData.username + "', '" + objRegisterData.password + "')");
            System.out.println("New user registered: " + objRegisterData.username);
            response = "REG_OK";
        }
        oos.writeObject(response);
    }

    private void PrivateChatMessageReceived(ObjPrivateChatMessage objPrivateChatMessage) throws IOException {
        ObjPrivateChatMessage receiver = new ObjPrivateChatMessage(myUsername, objPrivateChatMessage.message);
        ServerMain.clients.get(objPrivateChatMessage.otherUser).oos.writeObject(receiver);
    }

    private void LoginDataReceived(ObjLoginData objLoginData) throws IOException, SQLException {
        String response = "";
        Boolean exists = false;

        ResultSet rs = ServerMain.connection.createStatement().executeQuery("SELECT * FROM users WHERE username = '" + objLoginData.username + "' AND pass = '" + objLoginData.password + "'");
        if (rs.next()) {
            if (!ServerMain.clients.containsKey(objLoginData.username)) {
                System.out.println(objLoginData.username + " connected!\n");
                ServerMain.clients.put(objLoginData.username, this);
                response = "LOG_OK";
                myUsername = objLoginData.username;
                oos.writeObject(response);
                RefreshClients();
            } else {
                response = "LOGGED";
                oos.writeObject(response);
            }
        } else {
            oos.writeObject(response);
        }
    }

    private void GroupChatMessageReceived(ObjGroupChatMessage objGroupChatMessage) throws IOException {
        String message = myUsername + ": " + objGroupChatMessage.message;
        ObjGroupChatMessage newMessage = new ObjGroupChatMessage(message);

        for (ClientHandler clientHandler : ServerMain.clients.values()) {
            clientHandler.oos.writeObject(newMessage);
        }
    }

    private void RefreshClients() throws IOException {
        ArrayList<String> clients = new ArrayList<String>();
        for (String name : ServerMain.clients.keySet()) {
            clients.add(name);
        }
        for (ClientHandler clientHandler : ServerMain.clients.values()) {
            clientHandler.oos.writeObject(clients);
        }
    }

    private void OpenPrivateChatReceived(ObjOpenPrivateChat objOpenPrivateChat) throws IOException {
        for (String name : ServerMain.clients.keySet()) {
            if (name.equals(objOpenPrivateChat.chatWith)) {
                ObjOpenPrivateChat sender = new ObjOpenPrivateChat(myUsername);
                ServerMain.clients.get(name).oos.writeObject(sender);
            }
        }
    }
}
