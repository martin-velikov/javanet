import java.io.Serializable;

class TestObject implements Serializable {
    private static final long serialVersionUID = 1L;
  //  String recipient;
    String message;

    public TestObject(String message){
    //    this.recipient = recipient;
        this.message = message;
    }
}