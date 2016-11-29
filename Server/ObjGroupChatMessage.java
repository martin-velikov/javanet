import java.io.Serializable;

public class ObjGroupChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    String message;

    public ObjGroupChatMessage(String message) {
        this.message = message;
    }
}
