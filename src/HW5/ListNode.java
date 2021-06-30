package HW5;

import java.util.NoSuchElementException;

// hw2의 ListNode 이용
public class ListNode<P> {
    public P item;
    public ListNode<P> next;
    public ListNode(P newItem) {
        this.next = null;
        this.item = newItem;
    }

    public ListNode(P newItem, ListNode<P> next) {
        this.next = next;
        this.item = newItem;
    }

    public final P getItem() {
        return item;
    }

    public final void setItem(P item) {
        this.item = item;
    }

    public final void setNext(ListNode<P> next) {
        this.next = next;
    }

    public ListNode<P> getNext() {
        return this.next;
    }

    public final void insertNext(P obj) {
        ListNode<P> newListNode = new ListNode<>(obj);
        newListNode.next = this.next;
        this.next = newListNode;
    }

    public final void removeNext() {
        if (this.next == null) {
            throw new NoSuchElementException();
        }
        this.next = this.next.next;
    }
}
