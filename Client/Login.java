import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login extends Thread implements Initializable{
    Stage prevStage;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerButton;

    private String response = "";

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void run(){
        do {
            try {
                response = (String) Main.ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(!response.equals("OK"));
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ObjLoginData objLoginData = new ObjLoginData(username.getText(), password.getText());
                    Main.oos.writeObject(objLoginData);

                    Thread.sleep(100);  //Waiting for answer from server
                    if(response.equals("OK")) {
                        Main.myUsername = username.getText();

                        Parent root = FXMLLoader.load(getClass().getResource("chat_frame.fxml"));

                        Stage stage = new Stage();
                        stage.setTitle("Group chat");
                        stage.setScene(new Scene(root));

                        prevStage.close();

                        stage.show();
                    }else{
                        // ERROR MESSAGE
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("registration_panel.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Group chat");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        this.start();
    }
}
