package ui.animation;

import misc.RenderData;

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
//        renderData.scale = SettingsManager.getZoom();
//        renderData.zPow = SettingsManager.getZPow();
    }

    public Keyframe(float position, InterpolationType it, RenderData rd) {
        this.position = position;
        interpolationType = it;
        renderData = rd.copy();
        color = Color.RED;
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

    public void setRenderData(RenderData rd) {
        renderData = rd;
    }

    public InterpolationType getInterpolationType() { return interpolationType; }

    public void setInterpolationType(InterpolationType newInterpolationType) {
        interpolationType = newInterpolationType;
    }

    public RenderData getRenderData() { return renderData; }

    public void setPosition(float pos) {
        this.position = pos;
    }

    public float getPosition() {
        return position;
    }

    public Keyframe copy() {
        Keyframe temp = new Keyframe(position, interpolationType);
        temp.setEnabled(enabled);
        temp.setRenderData(renderData.copy());
        return temp;
    }

    public enum InterpolationType {
        LINEAR,
        EASE_IN_OUT,
        EASE_IN,
        EASE_OUT,
        CUBIC_HERMITE,
        JUMP,
        STALL,
        CIRCLE_SPHERE
    }
}

