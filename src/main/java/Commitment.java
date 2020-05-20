import java.util.ArrayList;
import java.util.List;

public class Commitment {
    int power;
    ArrayList<Integer> timestamp;
    int tolerance;

    public Commitment(int power, ArrayList<Integer> timestamp, int tolerance) {
        this.power = power;
        this.timestamp = timestamp;
        this.tolerance = tolerance;

    }

}
