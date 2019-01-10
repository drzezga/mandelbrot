package threads;

import colors.algorithms.ColorAlgorithm;
import ui.tabs.AnimationTab;
import ui.timeline.Keyframe;
import util.Complex;
import util.RenderData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimationRenderingThread extends RenderingManagerThread {
    public AnimationRenderingThread(BufferedImage _bufferedImage, Complex _center, int _w, int _h, double _zoom, int _threshold, ColorAlgorithm _colorAlgorithm, ThreadType threadType, int x1, int y1, int x2, int y2) {
        super(_bufferedImage, _center, _w, _h, _zoom, _threshold, _colorAlgorithm, threadType, x1, y1, x2, y2);
    }

    public RenderData[] generateFrameData(AnimationTab at) {
        int framerate = at.getFramerate();
        RenderData[] frameData = new RenderData[(int) (at.getTimeline().getLength() * framerate)];
        ArrayList<Keyframe> kfs = at.getTimeline().getKeyframes();

        for (int i = 0; i < kfs.size(); i++) {
            Keyframe kf = kfs.get(i);

            switch (kf.getInterpolationType()) {
                case JUMP:
                    RenderData rd = kf.getRenderData();
                    if (kfs.get(i + 1) == null) {
                        frameData[i * framerate] = rd;
                        break;
                    }
                    for (int j = 0; j < (kfs.get(i + 1).position - kf.position) * framerate; j++) {
                        frameData[i * framerate + j] = rd;
                    }
                    break;
                case EASEINOUT:

                    break;

                default:
                    System.err.println("Interpolation data is null or wrong");
            }
        }
        return frameData;
    }
}
