package ui.timeline;

import util.RenderData;

public class Keyframe {
    private RenderData renderData;
    private InterpolationType interpolationType;
    public float position;
    private boolean enabled = true;

    public Keyframe(float position, InterpolationType it) {
//        renderData = rd;
        this.position = position;
        interpolationType = it;
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

