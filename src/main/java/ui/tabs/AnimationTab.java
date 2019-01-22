package ui.tabs;

import threads.AnimationRenderingThread;
import ui.timeline.Keyframe;
import ui.timeline.Timeline;
import util.SettingsManager;

import javax.swing.*;
import java.awt.*;

public class AnimationTab extends JPanel {

    private Timeline timeline;
    private JFormattedTextField framerate;

    private JProgressBar progressBar;

    private boolean rendering = false;

    private AnimationRenderingThread animationRenderingThread;

    public AnimationTab() {
        JPanel controlPanel = new JPanel();

        timeline = new Timeline();
        setLayout(new BorderLayout());
        controlPanel.setLayout(new BorderLayout());

        JPanel actionPanel = new JPanel();

        JButton disableButton = new JButton("Disable all");
        JButton enableButton = new JButton("Enable all");
        JButton addButton = new JButton("New keyframe");
        disableButton.addActionListener(e -> {
            timeline.getKeyframes().forEach((kf) -> kf.setEnabled(false));
            timeline.repaint();
        });
        enableButton.addActionListener(e -> {
            timeline.getKeyframes().forEach((kf) -> kf.setEnabled(true));
            timeline.repaint();
        });
        addButton.addActionListener(e -> {
            timeline.addKeyframe(new Keyframe(timeline.getLength() + 0.5f, Keyframe.InterpolationType.EASE_IN_OUT));
        });


        actionPanel.add(disableButton);
        actionPanel.add(enableButton);
        actionPanel.add(addButton);
        actionPanel.add(new JLabel("Framerate (fps)"));

        framerate = new JFormattedTextField("60");
        framerate.setColumns(4);
        actionPanel.add(framerate);

        JButton renderButton = new JButton("Render");
        renderButton.addActionListener(e -> {
            if (animationRenderingThread != null) {
                if (animationRenderingThread.isAlive()) return;
            }
            if (rendering) return;

            animationRenderingThread = new AnimationRenderingThread(SettingsManager.getRenderingEngine(),this);
            animationRenderingThread.start();
        });
        actionPanel.add(renderButton);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(200, 24));

        progressBar.setString("Press the render button");
        progressBar.setStringPainted(true);
//        progressBar.setMaximum(1);
//        progressBar.setValue(1);
        actionPanel.add(progressBar);

        controlPanel.add(actionPanel, BorderLayout.CENTER);

        add(timeline, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.PAGE_END);
    }

    public int getFramerate() {
        return Integer.parseInt(framerate.getValue().toString());
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setMaxIterations(int n) {
        progressBar.setMaximum(n);
    }

    public void setProgress(int n) {
        progressBar.setValue(n);
        progressBar.setString(n + " / " + progressBar.getMaximum());
        rendering = true;
    }

    public void setTime(float seconds) {
        if (seconds == 0) {
            progressBar.setValue(0);
        } else {
            progressBar.setValue(progressBar.getMaximum());
            if (seconds > 60) {
                progressBar.setString("Render time: " + (int) Math.floor(seconds / 60) + "m, " + (int) Math.floor(seconds % 60) + "s");
            } else {
                progressBar.setString("Render time: " + seconds + "s");
            }
            rendering = false;
        }
    }
}
