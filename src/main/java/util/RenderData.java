package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RenderData {
    public Complex center;
    public double scale;
    public int threshold;
    public float zPow;

    // This constructor provides default values for before the ui is constructed and SettingsManager can be used
    public RenderData() {
        center = new Complex(new BigDecimal("-0.75"), new BigDecimal("0"));
        scale = 3;
        threshold = 200;
        zPow = 2;
    }

    public RenderData copy() {
        RenderData tempData = new RenderData();
        tempData.center = center.copy();
        tempData.scale = scale;
        tempData.threshold = threshold;
        tempData.zPow = zPow;
        return tempData;
    }

    public RenderData copy(Complex newPos, double newScale) {
        RenderData tempData = new RenderData();
        tempData.center = newPos;
        tempData.scale = newScale;
        tempData.threshold = threshold;
        tempData.zPow = zPow;
        return tempData;
    }

    // Time, duration
    public static RenderData lerp(RenderData from, RenderData to, float t, float d) {
        RenderData rd = new RenderData();

        // Center
        BigDecimal changeR = to.center.r.subtract(from.center.r);
        BigDecimal changeI = to.center.i.subtract(from.center.i);
//        BigDecimal changeR = from.center.r.subtract(to.center.r);
//        BigDecimal changeI = from.center.i.subtract(to.center.i);
        rd.center = new Complex(
            changeR.multiply(new BigDecimal(t)).divide(new BigDecimal(d), RoundingMode.HALF_EVEN).add(from.center.r),
            changeI.multiply(new BigDecimal(t)).divide(new BigDecimal(d), RoundingMode.HALF_EVEN).add(from.center.i)
        );

//        System.out.println((Math.min(from.scale, to.scale) - Math.max(from.scale, to.scale)));
        rd.scale = from.scale + (to.scale - from.scale) * t / d;
        rd.threshold = Math.round((to.threshold - from.threshold) * t / d + from.threshold);
        rd.zPow = (to.zPow - from.zPow) * t / d + from.zPow;

//      c*t/d + b;
        // val = start + change / steps * n
        return rd;
    }
}
