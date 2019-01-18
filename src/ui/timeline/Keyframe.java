package ui.timeline;

import util.RenderData;

import java.awt.*;

public class Keyframe {
    private RenderData renderData;
    private InterpolationType interpolationType;
    private float position;
    private boolean enabled = true;

    public Color color;

    public Keyframe(float position, InterpolationType it) {
//        renderData = rd;
        this.position = position;
        interpolationType = it;
        renderData = new RenderData();
        color = Color.RED;
//        renderData.center = new Complex(new BigDecimal("-0.75"), new BigDecimal("0"));
//        renderData.threshold = SettingsManager.getThreshold();
//        renderData.scale = SettingsManager.getScale();
//        renderData.zPow = SettingsManager.getZPow();
    }

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean en) {
        enabled = en;
    }

    public boolean getEnabled() {
        return enabled;
    }

//    public Keyframe(PixelRenderData rd) {
//        renderData = rd;
//        interpolationType = InterpolationType.EASEINOUT;
//    }

    public void setRenderData(RenderData rd) {
        renderData = rd;
    }

    public InterpolationType getInterpolationType() { return interpolationType; }

    public RenderData getRenderData() { return renderData; }

    public void setPosition(float pos) {
        this.position = pos;
    }

    public float getPosition() {
        return position;
    }

    public enum InterpolationType {
        LINEAR,
        EASEINOUT,
        EASEIN,
        EASEOUT,
        CUBICHERMITE,
        JUMP,
        WAIT
    }
}

