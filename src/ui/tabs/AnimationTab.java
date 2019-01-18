package ui.tabs;

import threads.AnimationRenderingThread;
import threads.RenderingManagerThread;
import ui.timeline.Timeline;
import util.SettingsManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimationTab extends JPanel {

    private JButton disableButton;
    private JButton enableButton;

    private Timeline timeline;
    private JPanel controlPanel;
    private JFormattedTextField framerate;

    private JProgressBar progressBar;
    private JButton renderButton;

    private boolean rendering = false;

    AnimationRenderingThread animationRenderingThread;

    public AnimationTab() {
        controlPanel = new JPanel();

        timeline = new Timeline();
        setLayout(new BorderLayout());
        controlPanel.setLayout(new BorderLayout());

        JPanel actionPanel = new JPanel();

        disableButton = new JButton("Disable all");
        enableButton = new JButton("Enable all");
        disableButton.addActionListener(e -> {
            timeline.getKeyframes().forEach((kf) -> kf.setEnabled(false));
            timeline.repaint();
        });
        enableButton.addActionListener(e -> {
            timeline.getKeyframes().forEach((kf) -> kf.setEnabled(true));
            timeline.repaint();
        });

        actionPanel.add(disableButton);
        actionPanel.add(enableButton);
        actionPanel.add(new JButton("New keyframe"));
        actionPanel.add(new JLabel("Framerate (fps)"));

        framerate = new JFormattedTextField("60");
        framerate.setColumns(4);
        actionPanel.add(framerate);

        renderButton = new JButton("Render");
        renderButton.addActionListener(e -> {
            if (animationRenderingThread != null) {
                if (animationRenderingThread.isAlive()) return;
            }

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
            progressBar.setString("Render time: " + seconds + "s");
            rendering = false;
        }
    }
}
