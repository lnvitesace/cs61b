public class LinkedListDeque<T> {
    private class Node {
        private Node prev;
        private T item;
        private Node next;

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node newFirst = new Node(sentinel, item, sentinel.next);
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
        ++size;
    }

    public void addLast(T item) {
        Node newLast = new Node(sentinel.prev, item, sentinel);
        sentinel.prev.next = newLast;
        sentinel.prev = newLast;
        ++size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node ptr = sentinel.next;
        System.out.print(ptr.item);
        while (ptr.next != sentinel) {
            ptr = ptr.next;
            System.out.print(" " + ptr.item);
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T result = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        --size;
        return result;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T result = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        --size;
        return result;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node ptr = sentinel;
        for (int i = 0; i <= index; ++i) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    private T getRecursiveHelper(Node start, int index) {
        if (index == 0) {
            return start.item;
        }
        return getRecursiveHelper(start.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }
}
