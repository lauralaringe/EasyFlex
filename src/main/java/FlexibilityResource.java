public class FlexibilityResource {
    String name;
    String type; // "b" for battery, "hp" for heat pump, "ev" for EV
    float output;

    public FlexibilityResource(String name, String type, float output) {
        this.name=name;
        this.type = type;
        this.output = output;
    }
}
