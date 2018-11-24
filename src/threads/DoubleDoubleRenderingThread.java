package threads;

import util.Complex;
import util.DoubleDouble;
import util.RenderData;

import java.awt.*;

public class DoubleDoubleRenderingThread extends RenderingThread {

    public DoubleDoubleRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    @Override
    public void calculate(RenderData renderData) {
        Complex c = new Complex(
                map(renderData.x, 0, renderData.w,
                        renderData.center.r.subtract(new DoubleDouble(renderData.scale / 2f * renderData.screenRatio)), renderData.center.r.add(new DoubleDouble(renderData.scale / 2f * renderData.screenRatio))),
                map(renderData.y, 0, renderData.h,
                        renderData.center.i.subtract(new DoubleDouble(renderData.scale / 2f)), renderData.center.i.add(new DoubleDouble(renderData.scale / 2f))));

        int n = calculateMandelbrot(c, renderData.threshold);

        Color color = renderData.colorAlgorithm.calculate(n, new Complex(), renderData.threshold);
        parent.setPixel(renderData.x, renderData.y, color);
    }

    public static int calculateMandelbrot(Complex c, int threshold) {
        Complex z = new Complex(0, 0);

        int n = 0;

        while (n < threshold) {
            // z = z^2 after simplified multiplication equation
            Complex z2 = new Complex(
                    z.r.sqr().subtract(z.i.sqr()),
                    z.r.multiply(z.i).multiply(new DoubleDouble(2)));

            // Adding c
            z.r = z2.r.add(c.r);
            z.i = z2.i.add(c.i);

            if (z.r.abs().add(z.i.abs()).compareTo(new DoubleDouble(8f)) == 1) {
                break;
            }
            n++;
        }
        return n;
    }

    static public final DoubleDouble map(double value, float start1, float stop1, DoubleDouble start2, DoubleDouble stop2) {
        DoubleDouble outgoing =
                start2.add(stop2.subtract(start2).multiply(new DoubleDouble(((float) value - start1) / (stop1 - start1))));
        return outgoing;
    }
}
