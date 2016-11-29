import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChat extends Thread implements Initializable {

    @FXML
    public Button button;
    @FXML
    public TextArea textArea;
    public String message = "";
    String chatWith;
    @FXML
    private TextArea username;
    @FXML
    private TextArea chatWindow;

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
        MainChat.privateChats.put(chatWith, this);
        username.setText("Chatting with: " + chatWith);
    }

    public void run() {
        chatWindow.setText(chatWindow.getText() + message + "\n");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ObjPrivateChatMessage objPrivateChatMessage = new ObjPrivateChatMessage(chatWith, textArea.getText());
                    Main.oos.writeObject(objPrivateChatMessage);
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                }
                chatWindow.setText(chatWindow.getText() + "You: " + textArea.getText() + "\n");
                chatWindow.setScrollTop(Double.MAX_VALUE);
                textArea.clear();
                event.consume();
            }
        });

        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        ObjPrivateChatMessage objPrivateChatMessage = new ObjPrivateChatMessage(chatWith, textArea.getText());
                        Main.oos.writeObject(objPrivateChatMessage);
                    } catch (IOException ioEx) {
                        ioEx.printStackTrace();
                    }
                    chatWindow.setText(chatWindow.getText() + "You: " + textArea.getText() + "\n");
                    chatWindow.setScrollTop(Double.MAX_VALUE);
                    textArea.clear();
                    event.consume();
                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        if (button.isVisible()) {
                            try {
                                ObjClosePrivateChat objClosePrivateChat = new ObjClosePrivateChat(Main.myUsername, chatWith);
                                Main.oos.writeObject(objClosePrivateChat);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        MainChat.privateChats.remove(chatWith);
                    }
                });
            }
        });
    }
}
