package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private final double factor;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
    }

    public double next() {
        state = (state + 1);
        double next = normalize(state % period);
        if (state % period == 0) {
            period = (int)(1.0 * period * factor);
            state = 0;
        }
        return next;
    }

    private double normalize(int s) {
        return 2.0 * s / (period - 1) - 1.0;
    }

}
