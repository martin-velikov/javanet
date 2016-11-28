import java.io.Serializable;

public class ObjPrivateChatMessage implements Serializable{
    private static final long serialVersionUID = 1L;
    String otherUser;
    String message;

    public ObjPrivateChatMessage(String otherUser, String message){
        this.otherUser = otherUser;
        this.message = message;
    }
}