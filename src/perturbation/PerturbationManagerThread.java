package perturbation;

import colors.algorithms.ColorAlgorithm;
import threads.ThreadType;
import ui.RenderPanel;
import ui.tabs.RenderingTab;
import util.Complex;
import util.RenderData;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PerturbationManagerThread extends Thread {

    private Complex[] x;

    BufferedImage image;
    private final Complex center;
    private final int w;
    private final int h;
    private final double scale;
    private float screenRatio;
    private final int threshold;
    private final ColorAlgorithm colorAlgorithm;
    private ThreadType threadType;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int pixelsLeft;

    long startTime;

    private PerturbationRenderingThread[] renderingThreads = new PerturbationRenderingThread[Runtime.getRuntime().availableProcessors()];

    public PerturbationManagerThread(BufferedImage _bufferedImage, Complex _center, int _w, int _h, double _zoom, int _threshold,
                                     ColorAlgorithm _colorAlgorithm, ThreadType threadType, int x1, int y1, int x2, int y2) {
        image = _bufferedImage;
        center = _center;
        w = _w;
        h = _h;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        screenRatio = (float) w / (float) h;
        scale = _zoom;
        threshold = _threshold;
        colorAlgorithm = _colorAlgorithm;
        pixelsLeft = (x1 - x2) * (y1 - y2);
        this.threadType = threadType;

        System.out.println(screenRatio);
        System.out.println("Running with " + Runtime.getRuntime().availableProcessors() + " cores");
    }

    @Override
    public void run() {
        super.run();
        RenderingTab renderingTab = RenderingTab.instance;
        startTime = System.nanoTime();
        renderingTab.setTime(0);
        renderingTab.setMaxIterations((x1 - x2) * (y1 - y2));

        for (int i = 0; i < renderingThreads.length; i++) {
            renderingThreads[i] = new PerturbationRenderingThread(this, x);
            renderingThreads[i].start();
        }

        while (areThreadsRunning()) {
            try {
                renderingTab.setProgress((x1 - x2) * (y1 - y2) - pixelsLeft);
                sleep(25);
            } catch (InterruptedException e) {}
        }
        RenderingTab.instance.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Rendering finished");
        RenderPanel.instance.repaint();
    }

    public RenderData fetchData() {
        if (pixelsLeft > 0) {
            pixelsLeft--;
            int wp = x2 - x1;
            int x = pixelsLeft % wp + x1;
            int y = (int) Math.floor(pixelsLeft / wp) + y1;

            return new RenderData(x, y, center, scale, screenRatio, w, h, threshold, colorAlgorithm);
        }
        return null;
    }

    public void setPixel(int x, int y, Color color) {
        if (color == null) {
            System.out.println("Color null: " + x + " " + y);
        } else {
            image.setRGB(x, y, color.getRGB());
        }
    }

    private boolean areThreadsRunning() {
        for (int i = 0; i < renderingThreads.length; i++) {
            if (renderingThreads[i].isAlive())
                return true;
        }
        return false;
    }
}
