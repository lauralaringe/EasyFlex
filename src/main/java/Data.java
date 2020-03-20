public class Data {
    String timestamp;
    String power;

    public Data(String timestamp, String power)
    {
        this.timestamp = timestamp;
        this.power = power;
    }

    public String timestamp() {
        return timestamp;
    }

    public String power() {
        return power;
    }

    public String toString() {
        return "Data [timestamp=" + timestamp + ", power=" + power + "]";
    }



}
