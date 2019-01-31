package ui.components;

import colors.palettes.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class PaletteSelectComboBox extends JPanel {

    public static PaletteSelectComboBox instance;

    public HashMap<String, String> algorithmHashMap = new HashMap<>();
    public HashMap<String, ColorPalette> paletteHashMap = new HashMap<>();

    public JComboBox<Object> algorithm;
    public JComboBox<Object> palette;

    public PaletteSelectComboBox() {
        instance = this;

        algorithmHashMap.put("Simple", "Simple");
        algorithmHashMap.put("Logarithmic", "Logarithmic");
        algorithmHashMap.put("Smooth", "Smooth");

        paletteHashMap.put("Black and white", new ColorPalette(
                new Color(0, 0, 0),
                new Color(255, 255, 255)
        ));
        paletteHashMap.put("BRG", new ColorPalette(
                new Color(0, 0, 255),
                new Color(255, 0, 0),
                new Color(0, 255, 0)));
        paletteHashMap.put("HSB", new HSBColorPalette());
        paletteHashMap.put("Wikipedia", new ConstantFillColorPalette(
                new Color(0, 0, 0),
                new Color(1, 6, 97),
                new Color(159, 243, 230),
                new Color(245, 197, 1)
        ));
        paletteHashMap.put("Looping Test", new LoopingColorPalette(512, 2,
                new Color(1, 6, 97),
                new Color(159, 243, 230),
                new Color(245, 197, 1)
        ));
        paletteHashMap.put("Test", new ExtendedConstantFillColorPalette(
                512,
                new Color(0, 0, 0),
                new Color(31, 54, 61),
                new Color(255, 255, 255),
                new Color(206, 255, 26),
                new Color(64, 121, 140),
                new Color(207, 153, 95),
                new Color(65, 64, 102),
                new Color(112, 169, 161),
                new Color(8, 99, 117),
                new Color(150, 137, 123),
                new Color(28, 202, 216)
        ));
        paletteHashMap.put("Subtle black", new ConstantFillColorPalette(
                new Color(33, 33, 33),
                new Color(33, 33, 33),
                new Color(0, 0, 0),
                new Color(0, 0, 0)
        ));

        ArrayList<String> algorithmComboBoxValues = new ArrayList<>();
        ArrayList<String> paletteComboBoxValues = new ArrayList<>();

        algorithmHashMap.forEach((k, v) -> algorithmComboBoxValues.add(k));
        paletteHashMap.forEach((k, v) -> paletteComboBoxValues.add(k));

        algorithm = new JComboBox<>(algorithmComboBoxValues.toArray());
        palette = new JComboBox<>(paletteComboBoxValues.toArray());

        algorithm.setSelectedItem("Simple");
        palette.setSelectedItem("Wikipedia");

        add(algorithm);
        add(palette);
    }
}
