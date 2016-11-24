import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainChat extends Thread implements Initializable{

    @FXML
    private TextArea chatWindow;
    @FXML
    private TextArea textArea;
    @FXML
    private Button button;
    @FXML
    private ListView clientList;

    public void run() {
        while (true){
            try {
                String response = Main.ois.readObject().toString() + "\n";
                chatWindow.setText(chatWindow.getText() + response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String text = textArea.getText();
                    Main.oos.writeObject(text);

                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                }
                chatWindow.setScrollTop(Double.MAX_VALUE);
                textArea.clear();
                event.consume();
            }
        });

        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    String text = textArea.getText();
                    try {
                        Main.oos.writeObject(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    chatWindow.setScrollTop(Double.MAX_VALUE);
                    textArea.clear();
                    event.consume();
                }
            }
        });

        ArrayList<String> clients = null;
        try {
            clients = (ArrayList<String>) Main.ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<String> items = FXCollections.observableArrayList(clients);
        clientList.setItems(items);

        this.start();
    }
}
