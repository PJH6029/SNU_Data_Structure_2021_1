package HW5;

public interface ListInterface<P> {
    public void add(int i, P x);
    public void append(P x);
    public P remove(int i);
    public boolean removeItem(P x);
    public P get(int i);
    public void set(int i, P x);
    public int indexOf(P x);
    public int size();
    public boolean isEmpty();
    public void clear();
}
