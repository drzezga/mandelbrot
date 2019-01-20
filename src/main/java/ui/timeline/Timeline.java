package ui.timeline;

import ui.RenderPanel;
import util.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Timeline extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private float position = 0; // Position of starting point in seconds
    private float scale = 20; // Seconds in visible timeline width
    private ArrayList<Keyframe> keyframes;

    private boolean isDragging = false;
    private Keyframe draggedKeyframe;
    private int lastDraggedX;

    private final Color lineColor = new Color(173, 173, 173);

    private Font font;

    FontMetrics fm;

    public Timeline() {
        keyframes = new ArrayList<>();
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        font = getFont();
        fm = getFontMetrics(font);

        addKeyframe(new ImmutableKeyframe(0, Keyframe.InterpolationType.EASE_IN_OUT));

//        addKeyframe(new Keyframe(1, Keyframe.InterpolationType.EASE_IN_OUT));
//        addKeyframe(new Keyframe(3.5f, Keyframe.InterpolationType.EASE_IN_OUT));
//        addKeyframe(new Keyframe(5, Keyframe.InterpolationType.EASE_IN_OUT));
        addKeyframe(new Keyframe(7, Keyframe.InterpolationType.EASE_IN_OUT));
        addKeyframe(new Keyframe(8, Keyframe.InterpolationType.EASE_IN_OUT));
//        addKeyframe(new Keyframe(10, Keyframe.InterpolationType.EASE_IN_OUT));
//        addKeyframe(new Keyframe(20, Keyframe.InterpolationType.EASE_IN_OUT));
//        addKeyframe(new Keyframe(21, Keyframe.InterpolationType.EASE_IN_OUT));
    }

    public float getLength() {
//        Keyframe max = new Keyframe(0, Keyframe.InterpolationType.EASE_IN_OUT);
        float maxPos = 0;
        for (int i = 0; i < keyframes.size(); i++) {
            if (maxPos < keyframes.get(i).getPosition()) {
                maxPos = keyframes.get(i).getPosition();
            }
        }
        return maxPos;
    }

    public ArrayList<Keyframe> getKeyframes() {
        return keyframes;
    }

    public void addKeyframe(Keyframe kf) {
        keyframes.add(kf);
        keyframes.sort(new KeyframeSort());
        repaint();
    }

    Keyframe findKeyframeAtTime(float time) {
        for (int i = 0; i < keyframes.size(); i++) {
            if (keyframes.get(i).getPosition() == time) return keyframes.get(i);
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (position < 0) position = 0;
        g.setColor(lineColor);
        g.setFont(font);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        float pixelSecond = getWidth() / scale;
        float lineX = pixelSecond * (1 - position % 1) - pixelSecond / 2 - 2;

        // Drawing the missing number when value at the start isn't shown fully
        g.setColor(Color.black);
        if (position % 1 != 0 && position >= 0) {
            g.drawString((int) Math.ceil(position) + "'", (int) lineX, getHeight() / 2 + 4);
        } else {
            g.drawString(1 + (int) Math.ceil(position) + "'", (int) lineX, getHeight() / 2 + 4);
        }

        // Drawing cells and numbers inside them
        for (int i = 0; i < scale; i++) {
            lineX = pixelSecond * (i + (1 - position % 1));
            g.setColor(lineColor);
            g.drawLine((int) lineX, 0, (int) lineX, getHeight());
            g.setColor(Color.black);
            if (position % 1 != 0 && position >= 0) {
                g.drawString((int) (i + 1 + Math.ceil(position)) + "'", (int) lineX + (int) (pixelSecond / 2) - fm.stringWidth((int) (i + 2 + Math.ceil(position)) + "'") / 2 + 2, getHeight() / 2 + 4);
            } else {
                g.drawString((int) (i + 2 + Math.ceil(position)) + "'", (int) lineX + (int) (pixelSecond / 2) - fm.stringWidth((int) (i + 2 + Math.ceil(position)) + "'") / 2 + 2, getHeight() / 2 + 4);
            }
        }

        // Drawing lines every half second
        for (int i = 0; i < scale + 1; i++) {
            lineX = pixelSecond * (i + (1 - position % 1) - 0.5f);
            g.setColor(lineColor);
            g.drawLine((int) lineX, 0, (int) lineX, getHeight() / 4);
        }

        // Drawing keyframes on cells
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe kf = keyframes.get(i);
            if (kf.getPosition() >= position && kf.getPosition() <= position + scale) {
//                int pos = (int) (pixelSecond * (kf.position % scale + (1 - position % 1) - 1));
                drawKeyframe(g, kf, pixelSecond);
            }
        }

        g.dispose();
    }

    private void drawKeyframe(Graphics g, Keyframe kf, float pixelSecond) {
        int x = (int) ((kf.getPosition() - position) * pixelSecond);
        if (kf.getEnabled()) {
            g.setColor(kf.color);
        } else {
            g.setColor(Color.gray);
        }
        int size = 6;
        int halfHeight = getHeight() / 2;

        int[] pointsX = {
                x, x + size, x, x - size
        };
        int[] pointsY = {
                halfHeight + size, halfHeight, halfHeight - size, halfHeight
        };
        g.fillPolygon(pointsX, pointsY, pointsX.length);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Right click
        if (e.getButton() == MouseEvent.BUTTON3) {
            float pixelSecond = getWidth() / scale;
            Keyframe kf = null;
            int i;

            for (i = 0; i < keyframes.size(); i++) {
                Keyframe localkf = keyframes.get(i);
                int pos = (int) ((localkf.getPosition() - position) * pixelSecond);
                if (localkf.getPosition() >= position && localkf.getPosition() <= position + scale) {
                    if (pos > e.getX() - 10 && pos < e.getX() + 10) {
                        kf = localkf;
                        break;
                    }
                }
            }
            if (kf == null) return;

            // Keyframe context menu handlers
            final int ii = i;
            final Keyframe finalKf = kf;
            KeyframeContextMenu kfcm = new KeyframeContextMenu(kf);
            kfcm.delButton.addActionListener((ActionEvent ae) -> {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this keyframe?", "Warning", dialogButton);
                if (dialogResult == 0) {
                    keyframes.remove(ii);
                    repaint();
                }
            });
            kfcm.propButton.addActionListener((ActionEvent ae) -> {
                new KeyframePropertiesWindow(finalKf);
                repaint();
            });
            kfcm.cloneButton.addActionListener((ActionEvent ae) -> {
                Keyframe newKf = finalKf.copy();
                float newPos = finalKf.getPosition();
                while (findKeyframeAtTime(newPos) != null) {
                    newPos += 0.5;
                }

                newKf.setPosition(newPos);
                addKeyframe(newKf);
                repaint();
            });
            kfcm.toggleButton.addActionListener((ActionEvent ae) -> {
                finalKf.toggle();
                repaint();
            });
            kfcm.setCurrentViewButton.addActionListener(ae -> {
//                System.out.println("Setting this keyframe to current view");
                finalKf.setRenderData(RenderPanel.instance.renderData.copy());
            });
            kfcm.setCurrentViewToThisButton.addActionListener(ae -> {
//                System.out.println("Setting current view to this keyframe");
                SettingsManager.setRenderData(finalKf.getRenderData().copy());
                RenderPanel.instance.render(); // TODO: This needs to go
            });
            kfcm.show(e.getComponent(), e.getX(), e.getY());
            return;
        }
        // Left click
        isDragging = true;
        lastDraggedX = e.getX();

        float pixelSecond = getWidth() / scale;
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe localKf = keyframes.get(i);
            int pos = (int) ((localKf.getPosition() - position) * pixelSecond);
            if (localKf.getPosition() >= position && localKf.getPosition() <= position + scale) {
                if (pos > e.getX() - 10 && pos < e.getX() + 10) {
                    draggedKeyframe = localKf;
                    return;
                }
            }
        }
        draggedKeyframe = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
        draggedKeyframe = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (isDragging) return;
        int notches = e.getWheelRotation();
        double scrollSpeed = 1.2;
        float oldX = e.getX();
        float pos = oldX / (getWidth() / scale);

        if (notches > 0) {
            if (scale < 25) {
                scale *= scrollSpeed;
                float deltaX = oldX - pos * (getWidth() / scale);
                position -= deltaX / (getWidth() / scale);
//                position += scale - oldScale;
            }
        } else {
            if (scale > 3) {
                scale /= scrollSpeed;
                float deltaX = pos * (getWidth() / scale) - oldX;
                position += deltaX / (getWidth() / scale);
            }
        }
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && draggedKeyframe == null) {
            if (position > 0 || e.getX() - lastDraggedX < 0) {
                position -= (e.getX() - lastDraggedX) / (getWidth() / scale);
                lastDraggedX = e.getX();
            }
            repaint();
        } else if (SwingUtilities.isLeftMouseButton(e) && draggedKeyframe != null) {
            float newPos = Math.round((position + e.getX() / (getWidth() / scale)) * 2) / 2f;
            if (newPos > 0) {
                if (findKeyframeAtTime(newPos) == null) draggedKeyframe.setPosition(newPos);
            }
            lastDraggedX = e.getX();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
