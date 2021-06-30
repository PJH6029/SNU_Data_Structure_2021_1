package HW2;

public class GenreList extends MyLinkedList<MovieList<MovieDBItem>> {
    @Override
    public void add(MovieList<MovieDBItem> item) {
        // genre 정렬해서 삽입
        // add 대상은 새로운 genre임이 명백해야함
        Node<MovieList<MovieDBItem>> currNode = head;
        while (currNode.getNext() != null && currNode.getNext().getItem().genre.compareTo(item.genre) < 0) {
            currNode = currNode.getNext();
        }
        currNode.insertNext(item);
        numItems++;
    }

    public MovieList<MovieDBItem> containGenre(String genre) {
        Node<MovieList<MovieDBItem>> currNode = head;
        while (currNode.getNext() != null) {
            currNode = currNode.getNext();
            if (currNode.getItem().genre.equals(genre)) {
                return currNode.getItem();
            }
        }
        return null;
    }
}
