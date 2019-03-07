package ui;

import com.sun.javafx.scene.traversal.Direction;
import threads.RenderingManagerThread;
import misc.Complex;
import misc.RenderData;
import misc.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static threads.BigDecimalRenderingThread.map;

public class RenderPanel extends JPanel implements MouseListener {

    public static RenderPanel instance;

    public RenderData renderData;

    // Changeable variables
//    public int threshold;

//    public double scale;

    private int width;
    private int height;

    public BufferedImage bufferedImage;
    private RenderingManagerThread renderingManagerThread;

    public RenderPanel() {
        instance = this;
        setPreferredSize(new Dimension(800, 800));
        renderData = new RenderData();

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage outImage;

        Graphics2D g2d = (Graphics2D) g;
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            render();
        }

        float proportions = (float)bufferedImage.getWidth() / (float)bufferedImage.getHeight();
        float screenProportions = (float)getWidth() / (float)getHeight();

        if (proportions > screenProportions) { // Needs to be scaled horizontally
            outImage = createResizedCopy(bufferedImage, getWidth(), bufferedImage.getHeight() * getWidth() / bufferedImage.getWidth());

        } else if (proportions < screenProportions) { // Needs to be scaled vertically
            if (bufferedImage.getHeight() > getHeight())
                outImage = createResizedCopy(bufferedImage, bufferedImage.getWidth() * getHeight() / bufferedImage.getHeight(), getHeight());
            else outImage = bufferedImage;

        } else { // Needs to be scaled just to fit the screen
            outImage = createResizedCopy(bufferedImage, getWidth(), getHeight());
        }

        if (outImage != null)
            if (outImage.getHeight() > getHeight())
                g2d.drawImage(outImage, getWidth() / 2 - outImage.getWidth() / 2, 0, this);
            else
                g2d.drawImage(outImage, getWidth() / 2 - outImage.getWidth() / 2, getHeight() / 2 - outImage.getHeight() / 2, this);
    }

    public void render() {
        if (renderingManagerThread != null) {
            if (renderingManagerThread.isAlive()) return;
        }
        renderData.threshold = SettingsManager.getThreshold();
        renderData.zoom = SettingsManager.getZoom();
        renderData.center = SettingsManager.getCenter();
        renderData.zPow = SettingsManager.getZPow();

        width = SettingsManager.getResolutionX();
        height = SettingsManager.getResolutionY();

        if (bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(),0, 0, width, height);

        renderingManagerThread.start();
    }

    private void shiftRender(Direction dir, int p) {
        renderData.threshold = SettingsManager.getThreshold();
        renderData.zoom = SettingsManager.getZoom();
        renderData.center = SettingsManager.getCenter();
        System.out.println(renderData.center.r + " " + renderData.center.i);

        width = SettingsManager.getResolutionX();
        height = SettingsManager.getResolutionY();

        if (bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
            render();
            return;
        }

        RenderingManagerThread renderingManagerThread;
        switch(dir) {
            case RIGHT:
                bufferedImage = shiftImage(bufferedImage, p, 0);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(),0, 0, p, height);
                break;
            case LEFT:
                bufferedImage = shiftImage(bufferedImage, -p, 0);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(), width - p, 0, width, height);
                break;
            case UP:
                bufferedImage = shiftImage(bufferedImage, 0, -p);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(), 0, height - p, width, height);
                break;
            case DOWN:
                bufferedImage = shiftImage(bufferedImage, 0, p);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(), 0, 0, width, p);
                break;
            default:
                renderingManagerThread = new RenderingManagerThread(bufferedImage, SettingsManager.getRenderingEngine(), 0, 0, width, height);
                break;
        }

        renderingManagerThread.start();
    }

    public void moveRight(int pixels) {
        SettingsManager.setCenter(new Complex(renderData.center.r.add(pixelsToPlaneLength(pixels)), renderData.center.i));
        shiftRender(Direction.LEFT, pixels);
    }

    public void moveLeft(int pixels) {
        SettingsManager.setCenter(new Complex(renderData.center.r.subtract(pixelsToPlaneLength(pixels)), renderData.center.i));
        shiftRender(Direction.RIGHT, pixels);
    }

    public void moveUp(int pixels) {
        SettingsManager.setCenter(new Complex(renderData.center.r, renderData.center.i.subtract(pixelsToPlaneLength(pixels))));
        shiftRender(Direction.DOWN, pixels);
    }

    public void moveDown(int pixels) {
        SettingsManager.setCenter(new Complex(renderData.center.r, renderData.center.i.add(pixelsToPlaneLength(pixels))));
        shiftRender(Direction.UP, pixels);
    }

    public static BufferedImage createResizedCopy(BufferedImage original, int newWidth, int newHeight) {
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
                original.getHeight(), null);
        g.dispose();
        return resized;
    }

    private static BufferedImage shiftImage(BufferedImage original, int shiftX, int shiftY) {
        BufferedImage shifted = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        Graphics2D g = shifted.createGraphics();
        g.drawImage(original, shiftX, shiftY, null);
        g.dispose();
        return shifted;
    }

    private BigDecimal pixelsToPlaneLength(int p) { // Works
        double scale = 3 / renderData.zoom;
        BigDecimal y1 = map(0, 0, height, renderData.center.i.subtract(new BigDecimal(scale / 2f)), renderData.center.i.add(new BigDecimal(scale / 2f)));
        BigDecimal y2 = map(p, 0, height, renderData.center.i.subtract(new BigDecimal(scale / 2f)), renderData.center.i.add(new BigDecimal(scale / 2f)));

        return y1.abs().subtract(y2.abs()).abs();
    }

    public void mouseClicked(MouseEvent me) {
        TabPanel.instance.grabFocus();
    }
    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) { TabPanel.instance.grabFocus(); }
    public void mouseExited(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
}
