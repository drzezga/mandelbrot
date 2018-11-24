package ui;

import com.sun.javafx.scene.traversal.Direction;
import threads.RenderingManagerThread;
import util.Complex;
import util.DoubleDouble;
import util.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static threads.DoubleDoubleRenderingThread.map;

public class RenderPanel extends JPanel implements MouseListener {

    public static RenderPanel instance;

    // Changeable variables
    public int threshold;
    public Complex center = new Complex(new DoubleDouble(-0.5), new DoubleDouble(0));
    public double scale;

    private int width;
    private int height;

    public BufferedImage bufferedImage;

    public RenderPanel() {
        instance = this;
        setPreferredSize(new Dimension(800, 800));

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
        threshold = SettingsManager.getThreshold();
        scale = SettingsManager.getScale();
        center = SettingsManager.getCenter();

        width = SettingsManager.getResolutionX();
        height = SettingsManager.getResolutionY();

        if (bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

//        ColorPalette palette = SettingsManager.getColorPalette();

        RenderingManagerThread renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(),0, 0, width, height);

        renderingManagerThread.start();
    }

    private void shiftRender(Direction dir, int p) {
        threshold = SettingsManager.getThreshold();
        scale = SettingsManager.getScale();
        center = SettingsManager.getCenter();
        System.out.println(center.r + " " + center.i);

        width = SettingsManager.getResolutionX();
        height = SettingsManager.getResolutionY();

        if (bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
            render();
            return;
        }

        RenderingManagerThread renderingManagerThread;
//        System.out.println(center.r.toString() + " " + center.i.toString());
        switch(dir) {
            case RIGHT:
                bufferedImage = shiftImage(bufferedImage, p, 0);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                        SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(), 0, 0, p, height);
                break;
            case LEFT:
                bufferedImage = shiftImage(bufferedImage, -p, 0);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                        SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(), width - p, 0, width, height);
                break;
            case UP:
                bufferedImage = shiftImage(bufferedImage, 0, -p);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                        SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(), 0, height - p, width, height);
                break;
            case DOWN:
                bufferedImage = shiftImage(bufferedImage, 0, p);
                renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                        SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(), 0, 0, width, p);
                break;
            default:
                renderingManagerThread = new RenderingManagerThread(bufferedImage, center, width, height, scale, threshold,
                        SettingsManager.getColorAlgorithm(), SettingsManager.getRenderingEngine(), 0, 0, width, height);
                break;
        }

        renderingManagerThread.start();
    }

    public void moveRight(int pixels) {
//        Complex center = SettingsManager.getCenter();
        SettingsManager.setCenter(new Complex(center.r.add(pixelsToPlaneLength(pixels)), center.i));
        shiftRender(Direction.LEFT, pixels);
    }

    public void moveLeft(int pixels) {
//        Complex center = ;
//        System.out.println(center.r.toString() + " " + center.i.toString());
        SettingsManager.setCenter(new Complex(center.r.subtract(pixelsToPlaneLength(pixels)), center.i));
//        center.r = center.r.subtract(pixelsToPlaneLength(pixels));
        shiftRender(Direction.RIGHT, pixels);
    }

    public void moveUp(int pixels) {
//        Complex center = SettingsManager.getCenter();
        SettingsManager.setCenter(new Complex(center.r, center.i.subtract(pixelsToPlaneLength(pixels))));
//        center.i = center.i.subtract(pixelsToPlaneLength(pixels));
        shiftRender(Direction.DOWN, pixels);
    }

    public void moveDown(int pixels) {
//        Complex center = SettingsManager.getCenter();
        SettingsManager.setCenter(new Complex(center.r, center.i.add(pixelsToPlaneLength(pixels))));
//        center.i = center.i.add(pixelsToPlaneLength(pixels));
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

    private DoubleDouble pixelsToPlaneLength(int p) { // Works
        DoubleDouble y1 = map(0, 0, height, center.i.subtract(new DoubleDouble(scale / 2f)), center.i.add(new DoubleDouble(scale / 2f)));
        DoubleDouble y2 = map(p, 0, height, center.i.subtract(new DoubleDouble(scale / 2f)), center.i.add(new DoubleDouble(scale / 2f)));

        return y1.abs().subtract(y2.abs()).abs();
    }


    public void mouseClicked(MouseEvent me) {
        ControlPanel.instance.grabFocus();
    }
    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
}
