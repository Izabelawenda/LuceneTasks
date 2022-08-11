public class KeyValuePairs {
    private String value;
    private String key;

    public KeyValuePairs(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "{" +
                "document='" + value + '\'' +
                ", text='" + key + '\'' +
                '}';
    }
}
