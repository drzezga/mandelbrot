package threads;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.*;
import ui.tabs.AnimationTab;
import ui.timeline.Keyframe;
import misc.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.ArrayList;

import static jdk.nashorn.internal.objects.Global.Infinity;

public class AnimationRenderingThread extends RenderingManagerThread {

    private RenderData[] frameData;
    private AnimationTab at;
    private int frameIndex = 0;

    private BufferedImage[] frames;

    public AnimationRenderingThread(ThreadType threadType, AnimationTab at) {
        super(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_3BYTE_BGR), threadType, 0, 0, SettingsManager.getResolutionX(), SettingsManager.getResolutionY());

        this.frameData = generateFrameData(at);
        this.at = at;
    }

    @Override
    public void run() {
        threadRun();
        startTime = System.nanoTime();
        at.setTime(0);
        at.setMaxIterations(Math.abs(frameData.length));
        frames = new BufferedImage[frameData.length];

        startThreads();

        while (areThreadsRunning()) {
            try {
                at.setProgress(frameIndex);
                sleep(25);
            } catch (InterruptedException ignored) {}
        }

        imagesToVideo();

        at.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Animation rendering finished");
        try {
            Desktop.getDesktop().open(new File("output.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextFrame() {
        if (frameIndex == frameData.length) return;
        image = postProcess(image);
        frames[frameIndex] = image;
        frameIndex++;

        image = createImage(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_INT_ARGB));
        pixelsLeft = w * h;
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
        for (int i = 0; i < frameData.length; i++) {
            try {
                if (frames[i] == null) {
                    System.err.println("Frame is empty " + i);
                    frames[i] = frames[i - 1];
//                    return;
                }
                boolean finished = false;
                while (!finished) {
                    try {
                        enc.encodeImage(frames[i]);
                        finished = true;
                    } catch (BufferOverflowException e) {
                        System.err.println(i);
                        finished = false;
                    }
                }
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
            try {
                image.setRGB(x, y, color.getRGB());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Coordinates: " + x + " | " + y);
            }
        }
    }

    private RenderData[] generateFrameData(AnimationTab at) {
        int framerate = at.getFramerate();
        RenderData[] frameData = new RenderData[(int) (at.getTimeline().getLength() * framerate)];
        ArrayList<Keyframe> kfs = at.getTimeline().getKeyframes();

        for (int i = 0; i < kfs.size(); i++) {
            Keyframe kf = kfs.get(i);
            RenderData rd = kf.getRenderData();

            if (i + 1 == kfs.size()) {
                frameData[Math.round(kf.getPosition() * framerate) - 1] = rd;
                continue;
            }

            Keyframe to = kfs.get(i + 1);
            int maxSize = (int) (to.getPosition() - kf.getPosition()) * framerate;

            switch (kf.getInterpolationType()) {
                case STALL:
                case JUMP:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = rd;
                    }
                    break;
                case EASE_IN:
                case EASE_OUT:
                case CUBIC_HERMITE:
                case EASE_IN_OUT:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.serp(rd, to.getRenderData(), j, maxSize);
                    }
                    break;
                case LINEAR:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, to.getRenderData(), j, maxSize);
                    }
                    break;
                case CIRCLE_SPHERE:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.circleSphere(rd, to.getRenderData(), j, maxSize);
                    }
                    break;
//                case RELATIVE_LINEAR:
//                    double[] scaleChanges = new double[maxSize];
//
//                    double fullScaleChange = to.getRenderData().scale - kf.getRenderData().scale;
//
//                    scaleChanges[0] = fullScaleChange / maxSize;
//                    System.out.println(fullScaleChange);
//                    double scaleSum = scaleChanges[0];
//                    // Computing properties, that aren't linear
//                    // The iteration 0 is purposely skipped, so that it is the original value
//                    for (int j = 1; j < maxSize; j++) {
//                        scaleChanges[j] = scaleChanges[j - 1] * 0.899;
////                        System.out.println(scaleChanges[j]);
//                        scaleSum += scaleChanges[j];
//                    }
////                    if (scaleSum == 0) throw new NumberFormatException("You can't change position from 0 to 0, because that divides by 0");
//
//                    computeScalingFactor(fullScaleChange, maxSize);
//                    double scaleFactor = fullScaleChange / scaleSum;
//
//                    // Multiplying properties, that aren't linear, so that they match the transformation length
//                    for (int j = 0; j < maxSize; j++) {
//                        scaleChanges[j] *= scaleFactor;
//                    }
//
//                    // Computing properties that are linear and applying those, that aren't
//                    RenderData prevRenderData = kf.getRenderData();
//                    for (int j = 0; j < maxSize; j++) {
//                        // This prevents NaN caused by division by 0
//                        if (scaleSum == 0) scaleChanges[j] = 0;
//
//                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, to.getRenderData(), j, maxSize);
//                        frameData[Math.round(kf.getPosition() * framerate + j)].scale = prevRenderData.scale + scaleChanges[j];
//                        prevRenderData = frameData[Math.round(kf.getPosition() * framerate + j)];
//                        if (j == 0) {
//                            System.err.println("dsa");
//                        }
////                        System.out.println(scaleChanges[j]);
//                    }

//                    double[] scales = new double[maxSize];
//                    double scaleChange = (kfs.get(i + 1).getRenderData().scale - kf.getRenderData().scale) / maxSize;
//                    double scaleSum = 0;
//                    RenderData prevFrameData = kf.getRenderData();
//
//                    for (int j = 0; j < maxSize; j++) {
//                        scales[j] = prevFrameData.scale * 0.5;
//                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, kfs.get(i + 1).getRenderData(), j, maxSize);
//                        frameData[Math.round(kf.getPosition() * framerate + j)].scale = prevFrameData.scale + (scaleChange *= 0.5);
//                        scaleSum += frameData[Math.round(kf.getPosition() * framerate + j)].scale;
//
//                        prevFrameData = frameData[j];
//                    }
//
//                    double scaleFactor = (kfs.get(i + 1).getRenderData().scale - kf.getRenderData().scale) / scaleSum;
//
//                    // Make the transformation reach the end again
//                    for (int j = 0; j < maxSize; j++) {
//                        frameData[Math.round(kf.getPosition() * framerate + j)].scale *= scaleFactor;
//                    }
//                    break;
                default:
                    System.err.println("Interpolation data is null or wrong");
            }
        }
        return frameData;
        // TODO: No compression
    }
    private static double computeScalingFactor(double full, int steps) {
        double step = steps / full;
        double result = step;
        double scalingFactor = 1;
        boolean finished = false;

        while (!finished) {
            step *= scalingFactor;
            for (int i = 1; i < steps; i++) {
                result += step;
                step *= 0.5;
            }
            if (result == full) {
                finished = true;
            } else {
//                if (scalingFactor == 0) {
//                    throw new NumberFormatException("Scaling factor is 0");
//                }
//                if (scalingFactor == Infinity) {
//                    throw new NumberFormatException("Scaling factor is Infinity");
//                }
                scalingFactor = (full / result);
                step = steps / full;
                result = step;
            }
            System.out.println("Scaling factor effectiveness: " + full / result);
            System.out.println("Scaling factor is now: " + scalingFactor);
        }
        return scalingFactor;
    }
}


















