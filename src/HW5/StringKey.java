package HW5;

public class StringKey implements Comparable<StringKey> {
    public String key;

    public StringKey(String k) {
        key = k;
    }

    @Override
    public int hashCode() {
        int hashed = 0;
        for (char c : key.toCharArray()) {
            hashed += c;
        }
        return hashed % 100;
    }

    @Override
    public int compareTo(StringKey stringKey) {
        if (stringKey == null) {
            return 1;
        }
        return this.key.compareTo(stringKey.key);
    }

    @Override
    public String toString() {
        return key;
    }
}
