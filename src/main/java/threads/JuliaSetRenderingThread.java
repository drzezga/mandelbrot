package threads;

import util.Complex;
import util.PixelRenderData;

import java.awt.*;

public class JuliaSetRenderingThread extends RenderingThread {

    public JuliaSetRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    void calculate(PixelRenderData renderData) {
        double centerR = renderData.center.r.doubleValue();
        double centerI = renderData.center.i.doubleValue();

        double cr = map(renderData.x, 0, renderData.w,
                -1.5 * renderData.screenRatio, 1.5 * renderData.screenRatio);
        double ci = map(renderData.y, 0, renderData.h,
                -1.5, 1.5);

        int n = calculateJulia(cr, ci, renderData.threshold, centerR, centerI);


        Color color = renderData.colorAlgorithm.calculate(n, new Complex(cr, ci), renderData.threshold);

        parent.setPixel(renderData.x, renderData.y, color);
    }

    private int calculateJulia(double cr, double ci, int threshold, double centerR, double centerI) {
        double zr = cr;
        double zi = ci;

        int n = 0;

        while (n < threshold) {
            double zr2 = zr * zr - zi * zi;
            double zi2 = zr * zi * 2;

            zr = zr2 + centerR;
            zi = zi2 + centerI;

            if (Math.abs(zr) + Math.abs(zi) > 32) {
                break;
            }
            n++;
        }
        return n;
    }

    static public final double map(double value,
                                   double start1, double stop1,
                                   double start2, double stop2) {
        double outgoing =
                start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }

    public boolean isShiftRenderingEnabled() {
        return false;
    }
}
