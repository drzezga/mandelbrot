package ui;

import ui.tabs.*;

import javax.swing.*;

public class TabPanel extends JTabbedPane {

    static TabPanel instance;

    JPanel rendering = new RenderingTab();
    JPanel display = new DisplayTab();
    JPanel position = new PositionTab();
    JPanel animation = new AnimationTab();

    public TabPanel() {
        instance = this;

        add("Image", rendering);
        add("Render settings", display);
        add("Position", position);
        add("Animation", animation);
    }
}
