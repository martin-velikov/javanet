import java.io.Serializable;

public class TestObject implements Serializable{
    private static final long serialVersionUID = 1L;
    String message;
    String username;

    public TestObject(String message, String username){
        this.message = message;
        this.username = username;
    }
}
