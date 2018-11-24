package ui;

import ui.tabs.*;

import javax.swing.*;

public class ControlPanel extends JTabbedPane {

    static ControlPanel instance;

    JPanel rendering = new RenderingTab();
    JPanel variables = new VariablesTab();
    JPanel position = new PositionTab();
    JPanel animation = new JPanel();
    JPanel coloring = new ColoringTab();
    JLabel animationLabel = new JLabel("Animation");
    JPanel settings = new SettingsTab();

    public ControlPanel() {
        instance = this;

        animation.add(animationLabel);

        add("Rendering", rendering);
        add("Variables", variables);
        add("Position", position);
        add("Animation", animation);
        add("Coloring", coloring);
        add("Settings", settings);
    }
}
