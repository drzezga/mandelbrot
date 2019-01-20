package ui;

import ui.tabs.*;

import javax.swing.*;

public class TabPanel extends JTabbedPane {

    static TabPanel instance;

    JPanel rendering = new RenderingTab();
    JPanel variables = new VariablesTab();
    JPanel position = new PositionTab();
    JPanel animation = new AnimationTab();
    JPanel coloring = new ColoringTab();
    JPanel settings = new SettingsTab();

    public TabPanel() {
        instance = this;

        add("Rendering", rendering);
        add("Variables", variables);
        add("Position", position);
        add("Animation", animation);
        add("Coloring", coloring);
        add("Settings", settings);
    }
}
