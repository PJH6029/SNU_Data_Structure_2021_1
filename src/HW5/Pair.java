package HW5;

public class Pair implements Comparable<Pair>{
    public int i;
    public int j;

    public Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public int compareTo(Pair pair) {
        if ((this.i == pair.i) && (this.j == pair.j)) return 0;
        else return 1;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ')';
    }


}
