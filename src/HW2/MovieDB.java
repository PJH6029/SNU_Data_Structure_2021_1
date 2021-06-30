package HW2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {

	private GenreList genreList;
	int numData;

    public MovieDB() {
		genreList = new GenreList();
    }

    public void insert(MovieDBItem item) {
        // Insert the given item to the MovieDB.
		MovieList<MovieDBItem> containingList = genreList.containGenre(item.getGenre());
		if (containingList != null) {
			containingList.add(item);
		} else {
			MovieList<MovieDBItem> newMovieList = new MovieList<>(item.getGenre());
			newMovieList.add(item);
			genreList.add(newMovieList);
		}
    }

    public void delete(MovieDBItem item) {
        // Remove the given item from the MovieDB.
		MovieList<MovieDBItem> containingList = genreList.containGenre(item.getGenre());
		if (containingList == null) {
			return;
		} else {
			for (Iterator<MovieDBItem> it = containingList.iterator(); it.hasNext(); ) {
				MovieDBItem movieDBItem = it.next();
				if (movieDBItem.equals(item)) {
					it.remove();
					break;
				}
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // Search the given term from the MovieDB.
		MyLinkedList<MovieDBItem> results = new MyLinkedList<>();
		for (MovieList<MovieDBItem> movieList : genreList) {
			for (MovieDBItem movieDBItem : movieList) {
				if (movieDBItem.getTitle().contains(term)) {
					results.add(movieDBItem);
				}
			}
		}
        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // Search the given term from the MovieDatabase.
        MyLinkedList<MovieDBItem> results = new MyLinkedList<>();
		for (MovieList<MovieDBItem> movieList : genreList) {
			for (MovieDBItem movieDBItem : movieList) {
				results.add(movieDBItem);
			}
		}
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	public Genre(String name) {
		super(name);
	}
	
	@Override
	public int compareTo(Genre o) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList<T extends Comparable<T>> implements ListInterface<T> {
	// 동일 item이 들어오면 새로추가하지 않음(Set 역할)

	Node<T> head;
	int numItems;
	String genre;

	public MovieList() {
		head = new Node<>(null);
	}

	public MovieList(String genre) {
		head = new Node<>(null);
		this.genre = genre;
	}

	@Override
	public Iterator<T> iterator() {
		return new MovieListIterator<>(this);
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public void add(T item) {
		if (this.contains(item)) {
			return;
		} else {
			Node<T> currNode = head;
			while (currNode.getNext() != null && currNode.getNext().getItem().compareTo(item) < 0) {
				currNode = currNode.getNext();
			}
			currNode.insertNext(item);
			numItems++;
		}
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
		numItems = 0;
	}

	public boolean contains(T item) {
		Node<T> currNode = head;
		while (currNode.getNext() != null) {
			currNode = currNode.getNext();
			if (currNode.getItem().equals(item)) {
				return true;
			}
		}
		return false;
	}
}

class MovieListIterator<T extends Comparable<T>> implements Iterator<T> {

	private MovieList<T> movieList;
	private Node<T> curr;
	private Node<T> prev;

	public MovieListIterator(MovieList<T> movieList) {
		this.movieList = movieList;
		this.curr = movieList.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

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
		movieList.numItems -= 1;
		curr = prev;
		prev = null;
	}
}

