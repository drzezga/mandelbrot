package threads;

import util.Complex;
import util.PixelRenderData;

import java.awt.*;

public class FloatRenderingThread extends RenderingThread {

    public FloatRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    void calculate(PixelRenderData renderData) {
        float centerR = (float)renderData.center.r.doubleValue();
        float centerI = (float)renderData.center.i.doubleValue();

        float cr = map(renderData.x, 0, renderData.w,
                (float)(centerR - (renderData.scale / 2f * renderData.screenRatio)), (float)(centerR + renderData.scale / 2f * renderData.screenRatio));
        float ci = map(renderData.y, 0, renderData.h,
                (float)(centerI - (renderData.scale / 2f)), (float)(centerI + renderData.scale / 2f));

        int n = calculateMandelbrot(cr, ci, renderData.threshold);

        Color color = renderData.colorAlgorithm.calculate(n, new Complex(cr, ci), renderData.threshold);

        parent.setPixel(renderData.x, renderData.y, color);

    }

    public static int calculateMandelbrot(float cr, float ci, int threshold) {
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
        return n;
    }

    static public final float map(float value,
                                  float start1, float stop1,
                                  float start2, float stop2) {
        float outgoing =
                start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }
}
