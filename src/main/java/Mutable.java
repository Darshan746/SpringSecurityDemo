import com.example.demo.Immutable;

public class Mutable extends Immutable {
    private int newValue;

    public Mutable(int value) {
        super(value);
        this.newValue = value;
    }

    @Override
    public int getValue1() {
        return newValue;
    }

    public void setValue(int newValue) {
        this.newValue = newValue;
    }
    public static void main(String[] arg){
        Mutable obj = new Mutable(4);
        Immutable immObj = (Immutable)obj;

        System.out.println(immObj.getValue1());
        obj.setValue(8);
        System.out.println(immObj.getValue1());
    }
}
