package creatures;

import huglife.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        assertEquals(new Action(Action.ActionType.STAY), c.chooseAction(surrounded));

        surrounded.put(Direction.TOP, new Plip(1));
        surrounded.put(Direction.LEFT, new Empty());

        assertEquals(new Action(Action.ActionType.ATTACK, Direction.TOP), c.chooseAction(surrounded));

        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.LEFT, new Impassible());

        assertEquals(new Action(Action.ActionType.REPLICATE, Direction.TOP), c.chooseAction(surrounded));

        Clorus c1 = new Clorus(0.5);

        assertEquals(new Action(Action.ActionType.MOVE, Direction.TOP), c1.chooseAction(surrounded));
    }
}
