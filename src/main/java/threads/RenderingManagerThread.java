package threads;

import colors.algorithms.ColorAlgorithm;
import ui.RenderPanel;
import ui.tabs.RenderingTab;
import util.Complex;
import util.PixelRenderData;
import util.SettingsManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderingManagerThread extends Thread {

    BufferedImage image;
    protected final Complex center;
    protected final int w;
    protected final int h;
    protected final double scale;
    protected float screenRatio;
    protected final int threshold;
    protected final ColorAlgorithm colorAlgorithm;
    protected ThreadType threadType;

    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;

    protected int pixelsLeft;

    protected long startTime;

    protected RenderingThread[] renderingThreads = new RenderingThread[Runtime.getRuntime().availableProcessors()];

    public RenderingManagerThread(BufferedImage _bufferedImage, Complex _center, int _w, int _h, double _zoom, int _threshold, ColorAlgorithm _colorAlgorithm, ThreadType threadType, int x1, int y1, int x2, int y2) {
        image = _bufferedImage;
        center = _center;
        w = _w;
        h = _h;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        screenRatio = (float)w / (float)h;
        scale = _zoom;
        threshold = _threshold;
        colorAlgorithm = _colorAlgorithm;
        pixelsLeft = (x1 - x2) * (y1 - y2);
        this.threadType = threadType;

        System.out.println("Running with " + Runtime.getRuntime().availableProcessors() + " cores");
    }

    public RenderingManagerThread(BufferedImage _bufferedImage, ThreadType threadType, int x1, int y1, int x2, int y2) {
        image = _bufferedImage;
        center = SettingsManager.getCenter();
        w = SettingsManager.getResolutionX();
        h = SettingsManager.getResolutionY();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        screenRatio = (float)w / (float)h;
        scale = SettingsManager.getScale();
        threshold = SettingsManager.getThreshold();
        colorAlgorithm = SettingsManager.getColorAlgorithm();
        pixelsLeft = (x2 - x1) * (y2 - y1);
        this.threadType = threadType;

        System.out.println(screenRatio);
        System.out.println("Running with " + Runtime.getRuntime().availableProcessors() + " cores");
    }

    @Override
    public void run() {
        superRun();
        RenderingTab renderingTab = RenderingTab.instance;
        startTime = System.nanoTime();
        renderingTab.setTime(0);
        renderingTab.setMaxIterations((x2 - x1) * (y2 - y1));

        startThreads();

        while (areThreadsRunning()) {
            try {
                renderingTab.setProgress((x2 - x1) * (y2 - y1) - pixelsLeft);
                sleep(25);
            } catch (InterruptedException e) {}
        }

        postProcess(image);

        RenderingTab.instance.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Rendering finished");
        RenderPanel.instance.repaint();
    }

    protected void superRun() {
        super.run();
    }

    protected void startThreads() {
        for (int i = 0; i < renderingThreads.length; i++) {
            switch (threadType) {
                case FLOAT:
                    renderingThreads[i] = new FloatRenderingThread(this);
                    break;
                case BIGDECIMAL:
                    renderingThreads[i] = new BigDecimalRenderingThread(this);
                    break;
                case PALETTE:
                    renderingThreads[i] = new PaletteRenderingThread(this);
                    break;
                case DOUBLE:
                    renderingThreads[i] = new DoubleRenderingThread(this);
                    break;
                case JULIASET:
                    renderingThreads[i] = new JuliaSetRenderingThread(this);
                    break;
            }
            if (i == 0 && !renderingThreads[i].isShiftRenderingEnabled()) {
                x1 = 0;
                y1 = 0;
                x2 = image.getWidth();
                y2 = image.getHeight();

                RenderingTab renderingTab = RenderingTab.instance;
                renderingTab.setMaxIterations((x2 - x1) * (y2 - y1));
                pixelsLeft = (x2 - x1) * (y2 - y1);
            }
            renderingThreads[i].start();
        }
    }

    protected boolean areThreadsRunning() {
        for (int i = 0; i < renderingThreads.length; i++) {
            if (renderingThreads[i].isAlive())
                return true;
        }
        return false;
    }

    protected void postProcess(BufferedImage img) {
        // TODO: Supersampling and crosshair
    }

    public PixelRenderData fetchData() {
        if (pixelsLeft > 0) {
            pixelsLeft--;
            int wp = x2 - x1;
            int x = pixelsLeft % wp + x1;
            int y = (int) Math.floor((float) pixelsLeft / wp) + y1;

            return new PixelRenderData(x, y, center, scale, screenRatio, w, h, threshold, colorAlgorithm, pixelsLeft == 0);
        }
        return null;
    }

    public void setPixel(int x, int y, Color color) {
        if (color == null) {
            System.out.println("Color null: " + x + " " + y);
        } else {
            try {
                image.setRGB(x, y, color.getRGB());
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                System.err.println("Coordinate out of bounds: " + x + " | " + y);
                throw new IndexOutOfBoundsException();
            }
        }
    }


}
