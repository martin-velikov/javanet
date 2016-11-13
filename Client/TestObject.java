import java.io.Serializable;

class TestObject implements Serializable {
    private static final long serialVersionUID = 1L;
    int recipient;
    String message;

    public TestObject(String message, int recipient){
        this.recipient = recipient;
        this.message = message;
    }
}