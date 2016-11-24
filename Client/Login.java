import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
                    LoginData loginData = new LoginData(username.getText(), password.getText());
                    Main.oos.writeObject(loginData);

                    Thread.sleep(100);  //Waiting for answer from server
                    if(response.equals("OK")) {
                        Parent root = FXMLLoader.load(getClass().getResource("chatFrame.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("Group chat");
                        stage.setScene(new Scene(root));

                        prevStage.close();

                        stage.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        this.start();
    }
}
