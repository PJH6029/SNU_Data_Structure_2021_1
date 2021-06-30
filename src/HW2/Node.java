package HW2;

import java.util.NoSuchElementException;

public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
		Node<T> newNode = new Node<>(obj);
		newNode.next = this.next; // append면 무의미한 라인
		this.next = newNode;
    }
    
    public final void removeNext() {
        if (this.next == null) {
            throw new NoSuchElementException();
        }
		this.next = this.next.next;
    }
}
