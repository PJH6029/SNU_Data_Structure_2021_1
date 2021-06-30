package HW5;

import java.util.ArrayList;

public class HashTable<K extends Comparable<K>, P extends Comparable<P>> {
    // Key, Pair

    public ArrayList<AVLTree<K, P>> table;
    final static int DEFAUlT_CAPACITY = 100;

    public HashTable(int n) {
        table = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            table.add(null);
        }
    }

    public HashTable() {
        table = new ArrayList<>(DEFAUlT_CAPACITY);
        for (int i = 0; i < DEFAUlT_CAPACITY; ++i) {
            table.add(null);
        }
    }

    public void insert(K key, P pair) {
        int slot = key.hashCode();
        if (table.get(slot) == null) {
            // AVL Tree 없음
            table.set(slot, new AVLTree<>());
        }
        table.get(slot).insert(key, pair);
    }

    public AVLTree<K, P> getSlotOf(int slotNum) {
        return table.get(slotNum);
    }

    public LinkedList<P> search(K key) {
        int slot = key.hashCode();
        if (table.get(slot) == null) {
            return null;
        } else {
            return table.get(slot).search(key);
        }
    }

    public String searchResultAsString(K key) {
        LinkedList<P> searchResult = search(key);
        if (searchResult == null) {
            return null;
        }
        return searchResult.toString().trim();
    }

}
