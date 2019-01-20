package ui.timeline;

import java.util.Comparator;

public class KeyframeSort implements Comparator<Keyframe> {
    @Override
    public int compare(Keyframe o1, Keyframe o2) {
        return (int) Math.ceil(o1.getPosition() - o2.getPosition());
    }
}
