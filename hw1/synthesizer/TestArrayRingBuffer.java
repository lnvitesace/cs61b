package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        for (int i = 0; i < 10; ++i) {
            arb.enqueue(i);
        }
        assertEquals((int) arb.dequeue(), 0);
        assertEquals((int) arb.dequeue(), 1);
        assertEquals((int) arb.dequeue(), 2);
        assertEquals((int) arb.peek(), 3);

        int tester = 3;
        for (int i : arb) {
            assertEquals(i, tester++);
        }
        assertEquals(tester, 10);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
