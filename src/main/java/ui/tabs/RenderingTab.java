package ui.tabs;

import ui.KeyHandler;
import ui.RenderPanel;
import ui.components.ResolutionButtons;
import ui.components.ResolutionTextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RenderingTab extends JPanel {

    public static RenderingTab instance;

    private ResolutionButtons resolutionButtons = new ResolutionButtons();

    private ResolutionTextField resolutionTextField = new ResolutionTextField();

    private JButton renderButton = new JButton("Render");

    private JProgressBar progressBar;

    private JButton saveButton = new JButton("Save");

    public boolean rendering = false;

    public RenderingTab() {
        instance = this;

        renderButton.setPreferredSize(new Dimension(100, 30));
        renderButton.addActionListener(e -> RenderPanel.instance.render());
        renderButton.addKeyListener(new KeyHandler());

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(200, 30));
        progressBar.setStringPainted(true);
        saveButton.setPreferredSize(new Dimension(60, 30));

        saveButton.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Image files", "png"));
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".png")) {
                    path += ".png";
                }
                File out = new File(path);
                try {
                    ImageIO.write(RenderPanel.instance.bufferedImage, "png", out);
                } catch (IOException exception) {}
            }
        });

        add(resolutionButtons);
        add(resolutionTextField);
        add(renderButton);
        add(progressBar);
        add(saveButton);
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
