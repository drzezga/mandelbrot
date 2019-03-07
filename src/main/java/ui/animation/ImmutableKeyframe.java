package ui.animation;

import java.awt.*;

public class ImmutableKeyframe extends Keyframe {
    public ImmutableKeyframe(float position, InterpolationType it) {
        super(position, it);
        color = Color.BLUE;
    }

    public void setPosition(float pos) {}

    public void toggle() {}

    public void setEnabled(boolean en) {}
}
