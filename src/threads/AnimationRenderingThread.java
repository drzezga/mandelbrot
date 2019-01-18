package threads;

import ui.tabs.AnimationTab;
import ui.timeline.Keyframe;
import util.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class AnimationRenderingThread extends RenderingManagerThread {

    private RenderData[] frameData;
    private AnimationTab at;
    private int frameIndex = 0;

    public AnimationRenderingThread(ThreadType threadType, AnimationTab at) {
        super(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_INT_ARGB),
                threadType, 0, 0, SettingsManager.getResolutionX(), SettingsManager.getResolutionY());

        this.frameData = generateFrameData(at);
        this.at = at;
    }

    @Override
    public void run() {
        superRun();
        startTime = System.nanoTime();
        at.setTime(0);
        at.setMaxIterations(w * h * frameData.length);

        startThreads();

        while (areThreadsRunning()) {
            try {
                at.setProgress(w * h * frameData.length - pixelsLeft);
                sleep(25);
            } catch (InterruptedException e) {}
        }
        at.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Animation rendering finished");
    }

    public void nextFrame() {
        System.out.println("Frame rendered");
        frameIndex++;
        // TODO: Add the frame to the video here
        image = new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_INT_ARGB);
        pixelsLeft = w * h;
//        TODO: Celownik
    }

    public PixelRenderData fetchData() {
//        System.out.println("Frame data length: " + frameData.length);
        if (frameIndex >= frameData.length) {
            return null;
        } else if (pixelsLeft > 0) {
            pixelsLeft--;
            int x = pixelsLeft % w + x1;
            int y = (int) Math.floor((float) pixelsLeft / w) + y1;
//            System.out.println(pixelsLeft);
            if (frameData[frameIndex] == null) System.out.println(frameIndex);
            return new PixelRenderData(x, y, frameData[frameIndex].center, frameData[frameIndex].scale, screenRatio, w, h, frameData[frameIndex].threshold, colorAlgorithm, pixelsLeft == 0);
        } else {
            return new ThreadStallData(0, 0, new Complex(), 0f, 0f, 0, 0, 0, SettingsManager.getColorAlgorithm(), false);
        }
    }

    public void setPixel(int x, int y, Color color) {
        if (color == null) {
            System.err.println("Color null: " + x + " " + y);
        } else {
            image.setRGB(x, y, color.getRGB());
        }
    }

    private RenderData[] generateFrameData(AnimationTab at) {
        int framerate = at.getFramerate();
        RenderData[] frameData = new RenderData[(int) (at.getTimeline().getLength() * framerate)];
        ArrayList<Keyframe> kfs = at.getTimeline().getKeyframes();

        for (int i = 0; i < kfs.size(); i++) {
            Keyframe kf = kfs.get(i);

            switch (kf.getInterpolationType()) {
                case EASEINOUT:
                case JUMP:
                    RenderData rd = kf.getRenderData();
                    if (i + 1 == kfs.size()) {
                        frameData[i] = rd;
                        break;
                    }
                    for (int j = 0; j < (kfs.get(i + 1).getPosition() - kf.getPosition()) * framerate; j++) {
                        frameData[i * framerate + j] = rd;
                    }
                    break;

                default:
                    System.err.println("Interpolation data is null or wrong");
            }
        }
        return frameData;
    }
}
