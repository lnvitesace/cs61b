public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 7;
        nextLast = 0;
        size = 0;
    }
    private void resize(int newSize) {
        T[] newArray = (T[]) new Object[newSize];
        int newArrayIndex = 0;
        int index = increaseIndex(nextFirst);
        do {
            newArray[newArrayIndex++] = items[index];
            index = increaseIndex(index);
        }
        while (index != nextLast);

        items = newArray;
        nextFirst = newSize - 1;
        nextLast = size;
    }

    private int decreaseIndex(int index) {
        if (index == 0) {
            index = items.length - 1;
        } else {
            --index;
        }
        return index;
    }

    private int increaseIndex(int index) {
        if (index == items.length - 1) {
            index = 0;
        } else {
            ++index;
        }
        return index;
    }

    private int getIndex(int index) {
        int start = increaseIndex(nextFirst);
        if (start + index < items.length) {
            return start + index;
        } else {
            return index - (items.length - start);
        }
    }
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = decreaseIndex(nextFirst);
        ++size;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = increaseIndex(nextLast);
        ++size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        System.out.print(items[increaseIndex(nextFirst)]);
        int index = increaseIndex(nextFirst);
        for (index = increaseIndex(index); index != nextLast; index = increaseIndex(index)) {
            System.out.print(" " + items[index]);
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = increaseIndex(nextFirst);
        T result = items[nextFirst];
        items[nextFirst] = null;
        --size;
        if (size < items.length / 2 && size > 4) {
            resize(items.length / 2);
        }
        return result;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = decreaseIndex(nextLast);
        T result = items[nextLast];
        items[nextLast] = null;
        --size;
        if (size < items.length / 2 && size > 4) {
            resize(items.length / 2);
        }
        return result;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[getIndex(index)];
    }

}
