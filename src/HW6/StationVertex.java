package HW6;

import java.util.Objects;

public class StationVertex {
    String id;

    public StationVertex(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        // id 같으면 equals
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationVertex that = (StationVertex) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "(" + id + ")";
    }

}
