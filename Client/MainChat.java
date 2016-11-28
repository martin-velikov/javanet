import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static HashMap<String, PrivateChat> privateChats = new HashMap<String, PrivateChat>();

    public void run() {
        while (true){
            try {
                final Object receivedObject = Main.ois.readObject();

                if(receivedObject.getClass() == ObjGroupChatMessage.class){
                    String response = ((ObjGroupChatMessage) receivedObject).message + "\n";
                    chatWindow.setText(chatWindow.getText() + response);
                }else if(receivedObject.getClass() == ArrayList.class){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> clients = (ArrayList<String>) receivedObject;
                            ObservableList<String> items = FXCollections.observableArrayList(clients);
                            clientList.setItems(items);
                        }
                    });
                }else if(receivedObject.getClass() == ObjOpenPrivateChat.class){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String chatWith = ((ObjOpenPrivateChat)receivedObject).chatWith;
                            if(!privateChats.containsKey(chatWith)){
                                try {
                                    FXMLLoader myFxmlLoader = new FXMLLoader(getClass().getResource("private_chat.fxml"));
                                    Parent root = myFxmlLoader.load();
                                    PrivateChat controller = myFxmlLoader.getController();
                                    controller.setChatWith(chatWith);

                                    Stage stage = new Stage();
                                    stage.setTitle("Private chat");
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                } catch (IOException ioEe) {
                                    ioEe.printStackTrace();
                                }
                            }
                        }
                    });
                }else if(receivedObject.getClass() == ObjPrivateChatMessage.class){
                    String receiver = ((ObjPrivateChatMessage)receivedObject).otherUser;
                    privateChats.get(receiver).message = receiver + ": " + ((ObjPrivateChatMessage)receivedObject).message;
                    privateChats.get(receiver).run();
                }
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
                    ObjGroupChatMessage objGroupChatMessage = new ObjGroupChatMessage(textArea.getText());
                    Main.oos.writeObject(objGroupChatMessage);

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
                    try {
                        ObjGroupChatMessage objGroupChatMessage = new ObjGroupChatMessage(textArea.getText());
                        Main.oos.writeObject(objGroupChatMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    chatWindow.setScrollTop(Double.MAX_VALUE);
                    textArea.clear();
                    event.consume();
                }
            }
        });

        clientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String chatWith = clientList.getSelectionModel().getSelectedItem().toString();
                    if(!chatWith.equals(Main.myUsername) && !privateChats.containsKey(chatWith)) {

                        ObjOpenPrivateChat objOpenPrivateChat = new ObjOpenPrivateChat(clientList.getSelectionModel().getSelectedItem().toString());
                        Main.oos.writeObject(objOpenPrivateChat);

                        FXMLLoader myFxmlLoader = new FXMLLoader(getClass().getResource("private_chat.fxml"));
                        Parent root = myFxmlLoader.load();

                        PrivateChat controller = myFxmlLoader.getController();
                        controller.setChatWith(chatWith);

                        Stage stage = new Stage();
                        stage.setTitle("Private chat");
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        this.start();
    }
}
