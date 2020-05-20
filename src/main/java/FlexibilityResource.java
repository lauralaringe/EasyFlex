import java.util.List;

public class FlexibilityResource {
    String name;
    String type; // "b" for battery, "hp" for heat pump, "ev" for EV
    List timestamp; // list of timestamps
    List power; // list of energy output in kWh

    public FlexibilityResource(String name, String type, List timestamp, List power) {
        this.name=name;
        this.type = type;
        this.timestamp = timestamp;
        this.power = power;
    }
}
