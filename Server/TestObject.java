import java.io.Serializable;

public class TestObject implements Serializable{
    String recipient;
    String message;

    public TestObject(String recipient, String message){
        this.recipient = recipient;
        this.message = message;
    }
}
