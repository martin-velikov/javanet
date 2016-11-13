import java.io.Serializable;

public class TestObject implements Serializable{
    private static final long serialVersionUID = 1L;
    String message;
    int recipient;

    public TestObject(String message, int recipient){
        this.message = message;
        this.recipient = recipient;
    }
}
