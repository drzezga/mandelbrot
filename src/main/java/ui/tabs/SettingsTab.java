package ui.tabs;

import misc.SupersampleType;
import ui.components.RenderingEngineSelectComboBox;

import javax.swing.*;

public class SettingsTab extends JPanel {

    public static SettingsTab instance;

    public RenderingEngineSelectComboBox renderingEngineSelectComboBox;
    public JCheckBox crosshairCheckBox;
    public JComboBox<SupersampleType> supersampleType;

    public SettingsTab() {
        instance = this;

        add(new JLabel("Rendering engine"));
        renderingEngineSelectComboBox = new RenderingEngineSelectComboBox();
        crosshairCheckBox = new JCheckBox();
        add(renderingEngineSelectComboBox);
        add(new JLabel("Crosshair (works best with animations)"));
        add(crosshairCheckBox);

        supersampleType = new JComboBox<>(SupersampleType.values());
        add(new JLabel("Supersample type"));
        add(supersampleType);
    }
}
