package ui.tabs;

import ui.components.PaletteSelectComboBox;

import javax.swing.*;

public class ColoringTab extends JPanel {

    PaletteSelectComboBox paletteSelectComboBox;

    public ColoringTab() {
        add(new JLabel("Color algorithm"));
        paletteSelectComboBox = new PaletteSelectComboBox();
        add(paletteSelectComboBox);
    }

}
