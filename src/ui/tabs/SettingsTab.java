package ui.tabs;

import ui.components.RenderingEngineSelectComboBox;

import javax.swing.*;

public class SettingsTab extends JPanel {

    public static SettingsTab instance;

    public RenderingEngineSelectComboBox renderingEngineSelectComboBox;

    public SettingsTab() {
        instance = this;

        add(new JLabel("Rendering engine"));
        renderingEngineSelectComboBox = new RenderingEngineSelectComboBox();
        add(renderingEngineSelectComboBox);
    }
}
