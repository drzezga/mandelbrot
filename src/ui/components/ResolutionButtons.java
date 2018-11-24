package ui.components;

import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;

public class ResolutionButtons extends JPanel {
    JButton HDButton = new JButton("HD");
    JButton HDPlusButton = new JButton("HD+");
    JButton FHDButton = new JButton("FHD");
    JButton UHD4KButton = new JButton("4K");
    JButton UHD5KButton = new JButton("5K");
    JButton UHD8KButton = new JButton("8K");
    JButton fullScreenButton = new JButton("Fit");
    JButton defaultButton = new JButton("Default");
    public ResolutionButtons() {
        setLayout(new GridLayout(2, 4));

        add(HDButton);
        add(HDPlusButton);
        add(FHDButton);
        add(fullScreenButton);
        add(UHD4KButton);
        add(UHD5KButton);
        add(UHD8KButton);
        add(defaultButton);

        HDButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(1280);
            ResolutionTextField.instance.yTextField.setValue(720);
            RenderPanel.instance.render();
        });
        HDPlusButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(1600);
            ResolutionTextField.instance.yTextField.setValue(900);
            RenderPanel.instance.render();
        });
        FHDButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(1920);
            ResolutionTextField.instance.yTextField.setValue(1080);
            RenderPanel.instance.render();
        });
        UHD4KButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(3840);
            ResolutionTextField.instance.yTextField.setValue(2160);
            RenderPanel.instance.render();
        });
        UHD5KButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(5120);
            ResolutionTextField.instance.yTextField.setValue(2880);
            RenderPanel.instance.render();
        });
        UHD8KButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(7680);
            ResolutionTextField.instance.yTextField.setValue(4320);
            RenderPanel.instance.render();
        });
        defaultButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(800);
            ResolutionTextField.instance.yTextField.setValue(800);
            RenderPanel.instance.render();
        });
        fullScreenButton.addActionListener(e -> {
            ResolutionTextField.instance.xTextField.setValue(RenderPanel.instance.getWidth());
            ResolutionTextField.instance.yTextField.setValue(RenderPanel.instance.getHeight());
            RenderPanel.instance.render();
        });
    }
}
