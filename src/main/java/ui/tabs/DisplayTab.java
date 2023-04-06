package ui.tabs;

import misc.SupersampleType;
import ui.components.PaletteSelectComboBox;
import ui.components.RenderingEngineSelectComboBox;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class DisplayTab extends JPanel {
    public static DisplayTab instance;

    public RenderingEngineSelectComboBox renderingEngineSelectComboBox;
    public JCheckBox crosshairCheckBox;
    public JComboBox<SupersampleType> supersampleType;
    public PaletteSelectComboBox paletteSelectComboBox;

    public DisplayTab() {
        instance = this;

        add(new JLabel("Rendering engine"));
        renderingEngineSelectComboBox = new RenderingEngineSelectComboBox();
        crosshairCheckBox = new JCheckBox();
        add(renderingEngineSelectComboBox);
        add(new JLabel("Crosshair"));
        add(crosshairCheckBox);

        supersampleType = new JComboBox<>(SupersampleType.values());
        add(new JLabel("Supersample type"));
        add(supersampleType);

        add(new JLabel("Color algorithm"));
        paletteSelectComboBox = new PaletteSelectComboBox();
        add(paletteSelectComboBox);
    }
}
