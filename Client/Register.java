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

public class Register implements Initializable {

    @FXML
    public Button button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField pass1;
    @FXML
    private PasswordField pass2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (pass1.getText().equals(pass2.getText()) &&
                            !username.getText().equals("") && !pass1.getText().equals("")) {
                        ObjRegisterData objRegisterData = new ObjRegisterData(username.getText(), pass1.getText());
                        Main.oos.writeObject(objRegisterData);
                    } else {
                        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("fxml/login_error.fxml"));
                        Parent root = myLoader.load();
                        ErrorController controller = myLoader.getController();
                        controller.text.setText("Incorrect data input !");

                        Stage stage = new Stage();
                        stage.setTitle("ERROR");
                        stage.setResizable(false);
                        stage.setScene(new Scene(root));

                        stage.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
