import java.io.Serializable;

class ObjLoginData implements Serializable {
    private static final long serialVersionUID = 1L;
    String username;
    String password;

    public ObjLoginData(String username, String password){
        this.username = username;
        this.password = password;
    }
}