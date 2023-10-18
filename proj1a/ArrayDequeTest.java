import org.junit.Test;
import static org.junit.Assert.*;
public class ArrayDequeTest {
    @Test
    public void testGet() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(0);
        a.addLast(2);
        a.addLast(3);

        int get0 = a.get(0);
        int get1 = a.get(1);
        int get2 = a.get(2);
        int get3 = a.get(3);

        assertEquals(0, get0);
        assertEquals(1, get1);
        assertEquals(2, get2);
        assertEquals(3, get3);

        assertNull(a.get(-1));
        assertNull(a.get(8));

        a.removeFirst();
        get0 = a.get(0);
        assertEquals(1, get0);

        a.removeLast();
        get1 = a.get(1);
        assertEquals(2, get1);
    }

    @Test
    public void testSize() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertTrue(a.isEmpty());
        a.addLast(3);
        assertEquals(1, a.size());
        a.addFirst(2);
        assertEquals(2, a.size());
        a.addFirst(1);
        assertEquals(3, a.size());
        assertFalse(a.isEmpty());

        a.removeFirst();
        assertEquals(2, a.size());
        a.removeLast();
        a.removeLast();
        assertTrue(a.isEmpty());
    }


    @Test
    public void testRemove() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(0);
        int a0 = a.removeFirst();
        assertEquals(0, a0);
        assertNull(a.removeFirst());
        assertNull(a.removeLast());

        a.addFirst(0);
        a0 = a.removeLast();
        assertEquals(0, a0);
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 9; ++i) {
            a.addLast(i);
        }
        a.printDeque();

    }
}
