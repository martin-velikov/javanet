import java.io.Serializable;

public class ObjOpenPrivateChat implements Serializable {
    private static final long serialVersionUID = 1L;
    String chatWith;

    public ObjOpenPrivateChat(String chatWith) {
        this.chatWith = chatWith;
    }
}
