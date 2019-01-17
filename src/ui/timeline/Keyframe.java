package ui.timeline;

import util.Complex;
import util.RenderData;
import util.SettingsManager;

import java.math.BigDecimal;

public class Keyframe {
    private RenderData renderData;
    private InterpolationType interpolationType;
    public float position;
    private boolean enabled = true;

    public Keyframe(float position, InterpolationType it) {
//        renderData = rd;
        this.position = position;
        interpolationType = it;
        renderData = new RenderData();
        renderData.center = new Complex(new BigDecimal("-0.75"), new BigDecimal("0"));
        renderData.threshold = SettingsManager.getThreshold();
        renderData.scale = SettingsManager.getScale();
        renderData.zPow = SettingsManager.getZPow();
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

    public enum InterpolationType {
        LINEAR,
        EASEINOUT,
        EASEIN,
        EASEOUT,
        CUBICHERMITE,
        JUMP
    }
}

