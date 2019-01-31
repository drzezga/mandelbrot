package misc;

import colors.algorithms.ColorAlgorithm;

public class ThreadStallData extends PixelRenderData {
    public ThreadStallData(int x, int y, Complex center, double scale, float screenRatio, int w, int h, int threshold, ColorAlgorithm colorAlgorithm, boolean last) {
        super(x, y, center, scale, screenRatio, w, h, threshold, colorAlgorithm, last);
    }
}
