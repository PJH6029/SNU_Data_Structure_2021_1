package HW6;

public class ListItem implements Comparable<ListItem> {
    public StationVertex vertex;
    public int time;

    public ListItem(StationVertex stationVertex, int time) {
        this.vertex = stationVertex;
        this.time = time;
    }

    @Override
    public int compareTo(ListItem that) {
        // time이 작으면 우선순위 높음
        return this.time <= that.time ? -1 : 1;
    }

}
