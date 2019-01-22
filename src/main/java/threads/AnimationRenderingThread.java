package threads;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.*;
import ui.tabs.AnimationTab;
import ui.timeline.Keyframe;
import util.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class AnimationRenderingThread extends RenderingManagerThread {

    private RenderData[] frameData;
    private AnimationTab at;
    private int frameIndex = 0;

    private BufferedImage[] frames;

    public AnimationRenderingThread(ThreadType threadType, AnimationTab at) {
        super(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_3BYTE_BGR),
                threadType, 0, 0, SettingsManager.getResolutionX(), SettingsManager.getResolutionY());

        this.frameData = generateFrameData(at);
        this.at = at;
    }

    @Override
    public void run() {
        superRun();
        startTime = System.nanoTime();
        at.setTime(0);
        at.setMaxIterations(Math.abs(frameData.length));
        frames = new BufferedImage[frameData.length];

//        startVideoEncoding(); // TODO: this

        startThreads();

        while (areThreadsRunning()) {
            try {
                at.setProgress(frameIndex);
                sleep(25);
            } catch (InterruptedException ignored) {}
        }
        imagesToVideo();
//        finishVideoEncoding();
        at.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Animation rendering finished");
    }

    public void nextFrame() {
//        System.out.println("Frame rendered");
        if (frameIndex == frameData.length) return;
        postProcess(image);
        frames[frameIndex] = image;
        frameIndex++;
//        addFrameToVideo();

        image = new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_INT_ARGB);
        pixelsLeft = w * h;
        // TODO: Celownik
    }

    private void imagesToVideo() {
        AWTSequenceEncoder enc;
        try {
            enc = new AWTSequenceEncoder(NIOUtils.writableFileChannel("output.mp4"), Rational.R(60, 1));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        at.setMaxIterations(frameData.length);
//        BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < frameData.length; i++) {
            try {
                if (frames[i] == null) {
                    System.err.println("Frame is empty " + i);
                    frames[i] = frames[i - 1];
//                    return;
                }
                enc.encodeImage(frames[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            at.setProgress(i);
        }
        try {
            enc.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public PixelRenderData fetchData() {
        if (frameIndex >= frameData.length) {
            return null;
        } else if (pixelsLeft > 0) {
            pixelsLeft--;
            int x = pixelsLeft % w + x1;
            int y = (int) Math.floor((float) pixelsLeft / w) + y1;
            if (frameData[frameIndex] == null) {
                System.out.println("Frame index null:" + frameIndex);
                return null;
            }
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
            RenderData rd = kf.getRenderData();

            switch (kf.getInterpolationType()) {
                case EASE_IN_OUT:
                case JUMP:
                    if (i + 1 == kfs.size()) {
                        frameData[Math.round(kf.getPosition() * framerate) - 1] = rd;
//                        break;
                    } else {
                        float maxSize = (kfs.get(i + 1).getPosition() - kf.getPosition()) * framerate;
                        for (int j = 0; j < maxSize; j++) {
                            frameData[Math.round(kf.getPosition() * framerate + j)] = rd;
                        }
                    }
                    break;
                case LINEAR:
                    if (i + 1 == kfs.size()) {
                        frameData[Math.round(kf.getPosition() * framerate) - 1] = rd;
                    } else {
                        float maxSize = (kfs.get(i + 1).getPosition() - kf.getPosition()) * framerate;
                        for (int j = 0; j < maxSize; j++) {
                            frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, kfs.get(i + 1).getRenderData(), j, maxSize);
//                            frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, kfs.get(i + 1).getRenderData(), j / maxSize,kfs.get(i + 1).getPosition() - kf.getPosition());
                        }
                    }
                    break;

                default:
                    System.err.println("Interpolation data is null or wrong");
            }
        }
        return frameData;
        // TODO: No compression
    }
}
