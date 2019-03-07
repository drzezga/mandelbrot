package threads;

import colors.algorithms.ColorAlgorithm;
import ui.RenderPanel;
import ui.tabs.RenderingTab;
import misc.Complex;
import misc.PixelRenderData;
import misc.SettingsManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class RenderingManagerThread extends Thread {

    protected BufferedImage image;
    protected Complex center;
    protected int w;
    protected int h;
    protected double scale;
    protected float screenRatio;
    protected int threshold;
    protected ColorAlgorithm colorAlgorithm;
    protected ThreadType threadType;

    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;

    protected int pixelsLeft;

    protected long startTime;

    protected RenderingThread[] renderingThreads = new RenderingThread[Runtime.getRuntime().availableProcessors()];

    public RenderingManagerThread(BufferedImage initialImage, ThreadType threadType, int _x1, int _y1, int _x2, int _y2) {
        x1 = _x1;
        y1 = _y1;
        x2 = _x2;
        y2 = _y2;

        image = createImage(initialImage);

        center = SettingsManager.getCenter();

        screenRatio = (float) w / (float) h;
        scale = SettingsManager.getZoom();
        threshold = SettingsManager.getThreshold();
        colorAlgorithm = SettingsManager.getColorAlgorithm();
        pixelsLeft = (x2 - x1) * (y2 - y1);
        this.threadType = threadType;

        System.out.println("Running with " + Runtime.getRuntime().availableProcessors() + " cores");
    }

    @Override
    public void run() {
        threadRun();
        RenderingTab renderingTab = RenderingTab.instance;
        startTime = System.nanoTime();
        renderingTab.setTime(0);
        renderingTab.setMaxIterations((x2 - x1) * (y2 - y1));

        startThreads();

        while (areThreadsRunning()) {
            try {
                renderingTab.setProgress((x2 - x1) * (y2 - y1) - pixelsLeft);
                sleep(25);
            } catch (InterruptedException ignored) {}
        }

        image = postProcess(image);

        RenderPanel.instance.bufferedImage = image;

        RenderingTab.instance.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Rendering finished");
        RenderPanel.instance.repaint();
    }

    protected void threadRun() {
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

    protected BufferedImage createImage(BufferedImage initialImage) {
        switch (SettingsManager.getSupersampleType()) {
            case DISABLED:
                w = SettingsManager.getResolutionX();
                h = SettingsManager.getResolutionY();
                break;
            case X2:
                w = SettingsManager.getResolutionX() * 2;
                h = SettingsManager.getResolutionY() * 2;
                x1 = x1 * 2;
                x2 = x2 * 2;
                y1 = y1 * 2;
                y2 = y2 * 2;
                break;
            case X4:
                w = SettingsManager.getResolutionX() * 4;
                h = SettingsManager.getResolutionY() * 4;
                x1 = x1 * 4;
                x2 = x2 * 4;
                y1 = y1 * 4;
                y2 = y2 * 4;
                break;
            case X6:
                w = SettingsManager.getResolutionX() * 6;
                h = SettingsManager.getResolutionY() * 6;
                x1 = x1 * 6;
                x2 = x2 * 6;
                y1 = y1 * 6;
                y2 = y2 * 6;
                break;
            case X8:
                w = SettingsManager.getResolutionX() * 8;
                h = SettingsManager.getResolutionY() * 8;
                x1 = x1 * 8;
                x2 = x2 * 8;
                y1 = y1 * 8;
                y2 = y2 * 8;
                break;
            case X12:
                w = SettingsManager.getResolutionX() * 12;
                h = SettingsManager.getResolutionY() * 12;
                x1 = x1 * 12;
                x2 = x2 * 12;
                y1 = y1 * 12;
                y2 = y2 * 12;
                break;
            case X16:
                w = SettingsManager.getResolutionX() * 16;
                h = SettingsManager.getResolutionY() * 16;
                x1 = x1 * 16;
                x2 = x2 * 16;
                y1 = y1 * 16;
                y2 = y2 * 16;
                break;
        }

        if (initialImage.getHeight() != h || initialImage.getWidth() != w) {
            x1 = 0;
            y1 = 0;
            x2 = w;
            y2 = h;
            return new BufferedImage(w, h, TYPE_3BYTE_BGR);
        } else {
            // Only true, when supersampling is disabled
            return initialImage;
        }
    }

    protected static BufferedImage postProcess(BufferedImage img) {
        switch (SettingsManager.getSupersampleType()) {
            case DISABLED:
                break;
            case X2:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 2, img.getHeight() / 2);
                break;
            case X4:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 4, img.getHeight() / 4);
                break;
            case X6:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 6, img.getHeight() / 6);
                break;
            case X8:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 8, img.getHeight() / 8);
                break;
            case X12:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 12, img.getHeight() / 12);
                break;
            case X16:
                img = RenderPanel.createResizedCopy(img, img.getWidth() / 16, img.getHeight() / 16);
                break;
        }
        if (SettingsManager.isCrosshairEnabled()) {
            Graphics g = img.createGraphics();
            int width = img.getWidth();
            int height = img.getHeight();
            int rectWidth = 4;
            int rectHeight = 10;
            int gap = 16;
            // Upper rectangle
            g.drawRect(width / 2 - rectWidth / 2, height / 2 - rectHeight - gap / 2, rectWidth, rectHeight);
            // Right rectangle
            g.drawRect(width / 2 + gap / 2, height / 2 - rectWidth / 2, rectHeight, rectWidth);
            // Bottom rectangle
            g.drawRect(width / 2 - rectWidth / 2, height / 2 + gap / 2, rectWidth, rectHeight);
            // Left rectangle
            g.drawRect(width / 2 - gap / 2 - rectHeight, height / 2 - rectWidth / 2, rectHeight, rectWidth);
        }
        return img;
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
