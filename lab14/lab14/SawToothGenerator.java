package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private final int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state = (state + 1);
        return normalize(state % period);
    }

    private double normalize(int s) {
        return 2.0 * s / (period - 1) - 1.0;
    }
}
