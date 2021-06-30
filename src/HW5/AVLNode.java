package HW5;

public class AVLNode<K extends Comparable<K>, P extends Comparable<P>> {
    // Key, Pair
    // Value는 LinkedList에서 연산할 때 compareTo를 해야해서 Comparable
    public K key;
    public LinkedList<P> item;
    public AVLNode<K, P> left, right;
    public int height;

    public AVLNode(K key) {
        this.key = key;
        item = new LinkedList<>();
        left = right = AVLTree.NIL;
        height = 1;
    }

    public AVLNode(K key, LinkedList<P> pairList) {
        this.key = key;
        item = pairList;
        left = right = AVLTree.NIL;
        height = 1;
    }

    public AVLNode(K key, LinkedList<P> pairList, AVLNode<K, P> leftChild, AVLNode<K, P> rightChild, int h) {
        this.key = key;
        item = pairList;
        left = leftChild; right = rightChild;
        height = h;
    }

    public void insertPair(P pair) {
        item.append(pair);
    }
}
