package util;

import java.math.BigDecimal;

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
}
