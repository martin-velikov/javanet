import java.io.Serializable;

public class LoginData implements Serializable{
    private static final long serialVersionUID = 1L;
    String username;
    String password;

    public LoginData(String username, String password){
        this.username = username;
        this.password = password;
    }
}
