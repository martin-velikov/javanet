import javafx.application.Platform;
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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login extends Thread implements Initializable {
    Stage prevStage;
    Boolean logged = false;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerButton;
    private String response = "";
    private Register register;


    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public void run() {
        do {
            try {
                response = (String) Main.ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessResponse(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } while (!response.equals("LOG_OK"));
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ObjLoginData objLoginData = new ObjLoginData(username.getText(), password.getText());
                    Main.oos.writeObject(objLoginData);
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                }
                event.consume();
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader myLoader = new FXMLLoader(getClass().getResource("registration_panel.fxml"));
                    Parent root = myLoader.load();
                    register = myLoader.getController();

                    Stage stage = new Stage();
                    stage.setTitle("Register form");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        if (!logged) {
                            System.exit(1);
                        }
                    }
                });
            }
        });

        this.start();
    }

    private void ProcessResponse(String response) throws IOException {
        if (response.equals("LOG_OK")) {
            Main.myUsername = username.getText();
            logged = true;

            Parent root = FXMLLoader.load(getClass().getResource("chat_frame.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Group chat");
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            prevStage.close();

            stage.show();
        } else if (response.equals("LOGGED")) {
            username.clear();
            password.clear();

            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("login_error.fxml"));
            Parent root = myLoader.load();
            ErrorController controller = myLoader.getController();
            controller.text.setText("User already logged in !");

            Stage stage = new Stage();
            stage.setTitle("ERROR");
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.show();
        } else if (response.equals("REG_OK")) {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("login_error.fxml"));
            Parent root = myLoader.load();
            ErrorController controller = myLoader.getController();
            controller.text.setText("Registration successful !");

            Stage stage = new Stage();
            stage.setTitle("SUCCESS");
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.show();

            Stage prevStage = (Stage) register.button.getScene().getWindow();
            prevStage.close();
        } else if (response.equals("TAKEN")) {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("login_error.fxml"));
            Parent root = myLoader.load();
            ErrorController controller = myLoader.getController();
            controller.text.setText("Username already registered !");

            Stage stage = new Stage();
            stage.setTitle("ERROR");
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.show();
        } else {
            username.clear();
            password.clear();

            Parent root = FXMLLoader.load(getClass().getResource("login_error.fxml"));

            Stage stage = new Stage();
            stage.setTitle("ERROR");
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.show();
        }
    }
}
