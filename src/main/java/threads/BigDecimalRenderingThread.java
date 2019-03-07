package threads;

import misc.Complex;
import misc.PixelRenderData;

import java.awt.*;
import java.math.BigDecimal;

public class BigDecimalRenderingThread extends RenderingThread {

    public BigDecimalRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    final static BigDecimal TWO = new BigDecimal("2");
    final static BigDecimal EIGHT = new BigDecimal("8");

    @Override
    public void calculate(PixelRenderData renderData) {
        double scale = 3 / renderData.zoom;
        Complex c = new Complex(
                map(renderData.x, 0, renderData.w,
                        renderData.center.r.subtract(new BigDecimal(scale / 2f * renderData.screenRatio)), renderData.center.r.add(new BigDecimal(scale / 2f * renderData.screenRatio))),
                map(renderData.y, 0, renderData.h,
                        renderData.center.i.subtract(new BigDecimal(scale / 2f)), renderData.center.i.add(new BigDecimal(scale / 2f))));

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
                    z.r.pow(2).subtract(z.i.pow(2)),
                    z.r.multiply(z.i).multiply(TWO));

            // Adding c
            z.r = z2.r.add(c.r);
            z.i = z2.i.add(c.i);

            if (z.r.abs().add(z.i.abs()).compareTo(EIGHT) == 1) {
                break;
            }
            n++;
        }
        return n;
    }

    static public final BigDecimal map(double value, float start1, float stop1, BigDecimal start2, BigDecimal stop2) {
        BigDecimal outgoing =
                start2.add(stop2.subtract(start2).multiply(new BigDecimal(((float) value - start1) / (stop1 - start1))));
        return outgoing;
    }
}
