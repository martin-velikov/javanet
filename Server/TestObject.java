import java.io.Serializable;

public class TestObject implements Serializable{
    private static final long serialVersionUID = 1L;
    String message;

    public TestObject(String message){
        this.message = message;
    }
}
