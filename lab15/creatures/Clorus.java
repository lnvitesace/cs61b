package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    private final int r;
    private final int g;
    private final int b;

    public Clorus(double energy) {
        super("clorus");
        this.energy = energy;
        r = 34;
        g = 0;
        b = 231;
    }

    public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c) {
        energy += c.energy();
    }

    public void move() {
        energy -= 0.03;
    }

    public void stay() {
        energy -= 0.01;
    }

    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            return new Action(Action.ActionType.ATTACK, HugLifeUtils.randomEntry(plips));
        } else if (energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empties));
        } else {
            return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(empties));
        }
    }
}
