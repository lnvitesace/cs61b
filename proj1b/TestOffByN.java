import org.junit.Test;
import static org.junit.Assert.*;
public class TestOffByN {
    static CharacterComparator offBy5 = new OffByN(5);

    @Test
    public void testOffByN() {
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertTrue(offBy5.equalChars('b', 'g'));
        assertFalse(offBy5.equalChars('f', 'h'));
    }
}
