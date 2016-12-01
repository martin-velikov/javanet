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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainChat extends Thread implements Initializable {

    public static HashMap<String, PrivateChat> privateChats = new HashMap<String, PrivateChat>();
    @FXML
    private TextArea chatWindow;
    @FXML
    private TextArea textArea;
    @FXML
    private Button button;
    @FXML
    private ListView clientList;

    public void run() {
        while (true) {
            try {
                final Object receivedObject = Main.ois.readObject();

                if (receivedObject.getClass() == ObjGroupChatMessage.class) {
                    String response = ((ObjGroupChatMessage) receivedObject).message + "\n";
                    chatWindow.setText(chatWindow.getText() + response);
                } else if (receivedObject.getClass() == ArrayList.class) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> clients = (ArrayList<String>) receivedObject;
                            ObservableList<String> items = FXCollections.observableArrayList(clients);
                            clientList.setItems(items);
                        }
                    });
                } else if (receivedObject.getClass() == ObjOpenPrivateChat.class) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String chatWith = ((ObjOpenPrivateChat) receivedObject).chatWith;
                            if (!privateChats.containsKey(chatWith)) {
                                try {
                                    FXMLLoader myFxmlLoader = new FXMLLoader(getClass().getResource("fxml/private_chat.fxml"));
                                    Parent root = myFxmlLoader.load();
                                    PrivateChat controller = myFxmlLoader.getController();
                                    controller.setChatWith(chatWith);

                                    Stage stage = new Stage();
                                    stage.setTitle("Private chat");
                                    stage.setResizable(false);
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                } catch (IOException ioEe) {
                                    ioEe.printStackTrace();
                                }
                            } else {
                                privateChats.get(chatWith).message = "\t" + chatWith + " RECONNECTED !";
                                privateChats.get(chatWith).run();
                                privateChats.get(chatWith).button.setVisible(true);
                                privateChats.get(chatWith).textArea.setVisible(true);
                            }
                        }
                    });
                } else if (receivedObject.getClass() == ObjPrivateChatMessage.class) {
                    String receiver = ((ObjPrivateChatMessage) receivedObject).otherUser;
                    privateChats.get(receiver).message = receiver + ": " + ((ObjPrivateChatMessage) receivedObject).message;
                    privateChats.get(receiver).run();
                } else if (receivedObject.getClass() == ObjClosePrivateChat.class) {
                    String sender = ((ObjClosePrivateChat) receivedObject).sender;
                    privateChats.get(sender).message = "\t" + sender + " DISCONNECTED !";
                    privateChats.get(sender).run();
                    privateChats.get(sender).button.setVisible(false);
                    privateChats.get(sender).textArea.setVisible(false);
                }
            } catch (Exception e) {
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
                    if (event.getClickCount() == 2 && clientList.getSelectionModel().getSelectedItem() != null) {
                        String chatWith = clientList.getSelectionModel().getSelectedItem().toString();
                        if (!chatWith.equals(Main.myUsername) && !privateChats.containsKey(chatWith)) {

                            ObjOpenPrivateChat objOpenPrivateChat = new ObjOpenPrivateChat(clientList.getSelectionModel().getSelectedItem().toString());
                            Main.oos.writeObject(objOpenPrivateChat);

                            FXMLLoader myFxmlLoader = new FXMLLoader(getClass().getResource("fxml/private_chat.fxml"));
                            Parent root = myFxmlLoader.load();

                            PrivateChat controller = myFxmlLoader.getController();
                            controller.setChatWith(chatWith);

                            Stage stage = new Stage();
                            stage.setTitle("Private chat");
                            stage.setResizable(false);
                            stage.setScene(new Scene(root));
                            stage.show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        for (Map.Entry<String, PrivateChat> entry : privateChats.entrySet()) {
                            if (entry.getValue().button.isVisible()) {
                                try {
                                    ObjClosePrivateChat objClosePrivateChat = new ObjClosePrivateChat(Main.myUsername, entry.getKey());
                                    Main.oos.writeObject(objClosePrivateChat);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        System.exit(1);
                    }
                });
            }
        });

        this.start();
    }
}
