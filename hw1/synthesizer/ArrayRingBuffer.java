package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */

    private int increaseIndex(int index) {
        if (index == this.capacity - 1) {
            return 0;
        }
        return index + 1;
    }

    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        rb[last] = x;
        last = increaseIndex(last);
        ++fillCount;
    }

    /**
     * Dequeue the oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T value = rb[first];
        first = increaseIndex(first);
        --fillCount;
        return value;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return rb[first];
    }

    // return an iterator of ArrayRingBuffer
    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    public class ArrayRingBufferIterator implements Iterator<T> {
        private int pointer;
        private int iterated;

        public ArrayRingBufferIterator() {
            pointer = first;
            iterated = 0;
        }

        public boolean hasNext() {
            return iterated != fillCount();
        }

        public T next() {
            T value = rb[pointer];
            pointer = increaseIndex(pointer);
            ++iterated;
            return value;
        }
    }

}
