package misc;

public class MandelbrotCalculationResult {
    public int iterations;
    public Complex zn;

    public MandelbrotCalculationResult(int iterations, Complex zn) {
        this.iterations = iterations;
        this.zn = zn;
    }
}