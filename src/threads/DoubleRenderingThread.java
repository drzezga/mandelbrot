package threads;

import util.Complex;
import util.RenderData;

import java.awt.*;

public class DoubleRenderingThread extends RenderingThread {

    public DoubleRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    void calculate(RenderData renderData) {
        double centerR = renderData.center.r.doubleValue();
        double centerI = renderData.center.i.doubleValue();

        double cr = map(renderData.x, 0, renderData.w,
                centerR - (renderData.scale / 2f * renderData.screenRatio), centerR + renderData.scale / 2f * renderData.screenRatio);
        double ci = map(renderData.y, 0, renderData.h,
                centerI - (renderData.scale / 2f), centerI + renderData.scale / 2f);

        int n = calculateMandelbrot(cr, ci, renderData.threshold);


        Color color = renderData.colorAlgorithm.calculate(n, new Complex(cr, ci), renderData.threshold);
//        if (cr > -0.01 && cr < 0.01) {
//            color = Color.RED;
//        }
//        else if (ci > -0.01 && ci < 0.01) {
//            color = Color.RED;
//        }

        parent.setPixel(renderData.x, renderData.y, color);

    }

    public static int calculateMandelbrot(double cr, double ci, int threshold) {
        double zr = 0;
        double zi = 0;

        int n = 0;

        while (n < threshold) {
            double zr2 = zr * zr - zi * zi;
            double zi2 = zr * zi * 2;

            zr = zr2 + cr;
            zi = zi2 + ci;

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
}
