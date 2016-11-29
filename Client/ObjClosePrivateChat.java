import java.io.Serializable;

public class ObjClosePrivateChat implements Serializable {
    private static final long serialVersionUID = 1L;
    String sender;
    String receiver;

    public ObjClosePrivateChat(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
