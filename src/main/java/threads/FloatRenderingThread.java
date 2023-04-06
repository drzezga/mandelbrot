package threads;

import misc.Complex;
import misc.MandelbrotCalculationResult;
import misc.PixelRenderData;

import java.awt.*;

public class FloatRenderingThread extends RenderingThread {

    public FloatRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    void calculate(PixelRenderData renderData) {
        float centerR = (float)renderData.center.r.doubleValue();
        float centerI = (float)renderData.center.i.doubleValue();

        double scale = 3 / renderData.zoom;
        float cr = map(renderData.x, 0, renderData.w,
                (float)(centerR - (scale / 2f * renderData.screenRatio)), (float)(centerR + scale / 2f * renderData.screenRatio));
        float ci = map(renderData.y, 0, renderData.h,
                (float)(centerI - (scale / 2f)), (float)(centerI + scale / 2f));

        MandelbrotCalculationResult res = calculateMandelbrot(cr, ci, renderData.threshold);

        Color color = renderData.colorAlgorithm.calculate(res.iterations, new Complex(cr, ci), renderData.threshold, res.zn);

        parent.setPixel(renderData.x, renderData.y, color);

    }

    public static MandelbrotCalculationResult calculateMandelbrot(float cr, float ci, int threshold) {
        float zr = 0;
        float zi = 0;

        int n = 0;

        while (n < threshold) {
            float zr2 = zr * zr - zi * zi;
            float zi2 = zr * zi * 2;

            zr = zr2 + cr;
            zi = zi2 + ci;

            if (Math.abs(zr) + Math.abs(zi) > 32) {
                break;
            }
            n++;
        }
        return new MandelbrotCalculationResult(n, new Complex(zr, zi));
    }

    static public float map(float value,
                            float start1, float stop1,
                            float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
}
