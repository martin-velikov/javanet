import java.io.Serializable;

class TestObject implements Serializable {
    private static final long serialVersionUID = 1L;
    String username;
    String message;

    public TestObject(String message, String username){
        this.username = username;
        this.message = message;
    }
}