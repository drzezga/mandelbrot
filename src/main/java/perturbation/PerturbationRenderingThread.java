package perturbation;
import misc.Complex;
import misc.PixelRenderData;

import java.awt.*;

public class PerturbationRenderingThread extends Thread {

    PerturbationManagerThread parent;

    private Complex[] x;

    public PerturbationRenderingThread(PerturbationManagerThread parent, Complex[] x) { this.parent = parent; this.x = x; }

    private boolean finished = false;

    public void run() {
        while (!finished) {
            PixelRenderData data = parent.fetchData();
            if (data != null) {
                calculate(data);
            } else {
                finished = true;
            }
        }
    }

    private double c0r;
    private double c0i;

    public void calculate(PixelRenderData renderData) {
//

        int window_radius = (renderData.w < renderData.h) ? renderData.w : renderData.h;

        // find the complex number at the center of this pixel
        /// basically the map function
        /// doesn't need to be precise
//        c0r = map(renderData.x, 0, renderData.w,
//                centerR - (renderData.scale / 2f * renderData.screenRatio), centerR + renderData.scale / 2f * renderData.screenRatio);
//        c0i = map(renderData.y, 0, renderData.h,
//                centerI - (renderData.scale / 2f), centerI + renderData.scale / 2f);
        c0r = renderData.scale * (2 * renderData.x - renderData.w) / renderData.screenRatio;
        c0i = -renderData.scale * (2 * renderData.y - renderData.h) / window_radius;
//        std::complex<double> d0 (radius * (2 * i - (int) size.x) / window_radius, -radius * (2 * j - (int) size.y) / window_radius);
//        int n = calculateMandelbrot(c, renderData.threshold);

        int iter = 0;

        /// max iteration more than x.length
        int max_iter = x.length;

        double zn_size;
        // run the iteration loop
        /// magic

        double cnr = c0r;
        double cni = c0i;
        do {
//            dn *= x[iter] + dn;
            cnr *= x[iter].r.doubleValue() + cnr;
            cni *= x[iter].i.doubleValue() + cni;
//            dn += d0;
            cnr += c0r;
            cni += c0i;
            ++iter;
            /// the result?
            // norm = r^2 + i^2
//            zn_size = std::norm(x[iter] * 0.5 + dn);
            zn_size = Math.pow(x[iter].r.doubleValue() * 0.5 + cnr, 2) + Math.pow(x[iter].i.doubleValue() * 0.5 + cni, 2);

            // use bailout radius of 256 for smooth coloring.
        } while (zn_size < 256 && iter < max_iter);

        Color color = renderData.colorAlgorithm.calculate((int)zn_size, new Complex(), renderData.threshold);
        parent.setPixel(renderData.x, renderData.y, color);
    }

//    public static int calculateMandelbrot(double c0r, double c0i, int threshold, Complex[] x) {
//        Complex z = new Complex(0, 0);
//
//        int n = 0;
//
//        while (n < threshold) {
//            // z = z^2 after simplified multiplication equation
//            Complex z2 = new Complex(
//                    z.r.sqr().subtract(z.i.sqr()),
//                    z.r.multiply(z.i).multiply(new DoubleDouble(2)));
//
//            // Adding c
//            z.r = z2.r.add(c.r);
//            z.i = z2.i.add(c.i);
//
//            if (z.r.abs().add(z.i.abs()).compareTo(new DoubleDouble(8f)) == 1) {
//                break;
//            }
//            n++;
//        }
//        return n;


//    }

//    static public final DoubleDouble map(double value, float start1, float stop1, DoubleDouble start2, DoubleDouble stop2) {
//        DoubleDouble outgoing =
//                start2.add(stop2.subtract(start2).multiply(new DoubleDouble(((float) value - start1) / (stop1 - start1))));
//        return outgoing;
//    }
    static public final double map(double value,
                                  double start1, double stop1,
                                  double start2, double stop2) {
        double outgoing =
                start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }
}
