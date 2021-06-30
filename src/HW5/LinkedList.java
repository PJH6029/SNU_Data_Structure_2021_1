package HW5;

import java.util.Iterator;
import java.util.NoSuchElementException;

// hw2의 LinkedList 변형
public class LinkedList<P extends Comparable<P>> implements ListInterface<P> {
    protected ListNode<P> head;
    protected int numItems;

    public LinkedList() {
        head = new ListNode<P>(null, null); // dummy
        numItems = 0;
    }

    public final Iterator<P> iterator() {
        return new LinkedListIterator<>(this);
    }


    @Override
    public void add(int index, P x) {
        if (index >= 0 && index <= numItems - 1) {
            ListNode<P> prev = getNode(index - 1);
            ListNode<P> newListListNode = new ListNode<P>(x, prev.next);
            prev.next = newListListNode;
            numItems++;
        }
    }

    @Override
    public void append(P x) {
        ListNode<P> newListListNode = new ListNode<P>(x);
        ListNode<P> prev = getNode(numItems - 1);
        prev.next = newListListNode;
        numItems++;
    }

    @Override
    public P remove(int index) {
        if (index >= 0 && index <= numItems - 1) {
            ListNode<P> prev = getNode(index - 1);
            ListNode<P> curr = prev.next;
            prev.next = curr.next;
            numItems--;
            return curr.item;
        } else {
            return null;
        }
    }

    @Override
    public boolean removeItem(P x) {
        ListNode<P> prev = head;
        while (prev.next != null) {
            if (prev.next.item.compareTo(x) == 0) {
                prev.next = prev.next.next;
                numItems--;
                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    @Override
    public P get(int index) {
        if (index >= 0 && index <= numItems - 1) {
            return getNode(index).item;
        } else return null;
    }

    @Override
    public void set(int index, P x) {
        if (index >= 0 && index <= numItems - 1) {
            getNode(index).item = x;
        }
    }

    private final int NOT_FOUND = -1;
    @Override
    public int indexOf(P x) {
        ListNode<P> currListListNode = head;
        for (int i = 0; i <numItems; ++i) {
            currListListNode = currListListNode.next;
            if (currListListNode.item.compareTo(x) == 0) return i;
        }
        return NOT_FOUND;
    }

    @Override
    public int size() {
        return numItems;
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    @Override
    public void clear() {
        numItems = 0;
        head = new ListNode<P>(null, null);
    }

    public boolean contains(P x) {
        ListNode<P> curr = head;
        while (curr.next != null) {
            curr = curr.next;
            if (curr.item.compareTo(x) == 0) {
                return true;
            }
        }
        return false;
    }


    protected ListNode<P> getNode(int index) {
        if (index >= -1 && index <= numItems - 1) {
            ListNode<P> curr = head;
            for (int i = 0; i <= index; ++i) {
                curr = curr.next;
            }
            return curr;
        } else {
            return null;
        }
    }

    public void printList() {
        ListNode<P> curr = head;
        while (curr.next != null) {
            curr = curr.next;
            System.out.print(curr.item + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode<P> curr = head;
        while (curr.next != null) {
            curr = curr.next;
            sb.append(curr.item.toString());
            sb.append(" ");
        }

        return sb.toString();
    }
}

class LinkedListIterator<E extends Comparable<E>> implements Iterator<E> {
    private LinkedList<E> list;
    private ListNode<E> curr;
    private ListNode<E> prev;

    public LinkedListIterator(LinkedList<E> list) {
        this.list = list;
        this.curr = list.head;
        this.prev = null;
    }

    @Override
    public boolean hasNext() {
        return curr.getNext() != null;
    }

    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();

        prev = curr;
        curr = curr.getNext();

        return curr.getItem();
    }

    @Override
    public void remove() {
        if (prev == null)
            throw new IllegalStateException("next() should be called first");
        if (curr == null)
            throw new NoSuchElementException();
        prev.removeNext();
        list.numItems -= 1;
        curr = prev;
        prev = null;
    }
}
