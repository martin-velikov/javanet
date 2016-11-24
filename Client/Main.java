import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application {

    public static InetAddress host;
    public static final int PORT = 9876;
    public static Socket socket = null;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;

    @Override
    public void start(Stage primaryStage) throws Exception {
        host = InetAddress.getLocalHost();
        socket = new Socket(host, PORT);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("loginPanel.fxml"));
        Parent root = myLoader.load();

        Login controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
