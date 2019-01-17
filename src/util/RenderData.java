package util;

public class RenderData {
    public Complex center;
    public double scale;
    public int threshold;
    public float zPow;

    public RenderData copy() {
        RenderData tempData = new RenderData();
        tempData.center = center;
        tempData.scale = scale;
        tempData.threshold = threshold;
        tempData.zPow = zPow;
        return tempData;
    }
}
