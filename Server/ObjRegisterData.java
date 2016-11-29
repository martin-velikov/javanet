import java.io.Serializable;

public class ObjRegisterData implements Serializable {
    private static final long serialVersionUID = 1L;
    String username;
    String password;

    public ObjRegisterData(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
