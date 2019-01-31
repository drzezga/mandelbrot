package misc;

import colors.algorithms.ColorAlgorithm;

public class PixelRenderData {
    public final int x;
    public final int y;
    public final Complex center;
    public final double scale;
    public final float screenRatio;
    public final int w;
    public final int h;
    public final int threshold;
    public ColorAlgorithm colorAlgorithm;
    public boolean last;

    public PixelRenderData(int x, int y, Complex center, double scale, float screenRatio, int w, int h, int threshold, ColorAlgorithm colorAlgorithm, boolean last) {
        this.x = x;
        this.y = y;
        this.center = center;
        this.scale = scale;
        this.screenRatio = screenRatio;
        this.w = w;
        this.h = h;
        this.threshold = threshold;
        this.colorAlgorithm = colorAlgorithm;
        this.last = last;
    }
}
