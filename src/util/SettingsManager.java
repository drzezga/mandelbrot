package util;

import colors.algorithms.ColorAlgorithm;
import colors.algorithms.LogarithmicColorAlgorithm;
import colors.algorithms.SimpleColorAlgorithm;
import colors.palettes.ColorPalette;
import threads.ThreadType;
import ui.RenderPanel;
import ui.components.PaletteSelectComboBox;
import ui.components.ResolutionTextField;
import ui.tabs.PositionTab;
import ui.tabs.SettingsTab;
import ui.tabs.VariablesTab;

public class SettingsManager {
    public static int getResolutionX() {
        if (ResolutionTextField.instance.xTextField.getValue().getClass() == java.lang.Long.class) {
            return ((Long)ResolutionTextField.instance.xTextField.getValue()).intValue();
        } else {
            return (int)ResolutionTextField.instance.xTextField.getValue();
        }
    }
    public static int getResolutionY() {
        if (ResolutionTextField.instance.yTextField.getValue().getClass() == java.lang.Long.class) {
            return ((Long)ResolutionTextField.instance.yTextField.getValue()).intValue();
        } else {
            return (int)ResolutionTextField.instance.yTextField.getValue();
        }
    }
    public static int getThreshold() {
        if (VariablesTab.instance.threshold.getValue().getClass() == java.lang.Long.class) {
            return ((Long)VariablesTab.instance.threshold.getValue()).intValue();
        } else {
            return (int)VariablesTab.instance.threshold.getValue();
        }
    }
    public static double getScale() {
        double val;
        try {
            val = Double.parseDouble(PositionTab.instance.scaleComboBox.getText());
        } catch (NumberFormatException e) {
            val = RenderPanel.instance.scale;
            PositionTab.instance.scaleComboBox.setText(Double.toString(val));
        }
        return val;
    }
    public static void setScale(double scale) {
        PositionTab.instance.scaleComboBox.setValue(scale);
    }
    public static Complex getCenter() {
        DoubleDouble real;
        DoubleDouble imag;
        try {
            real = new DoubleDouble(PositionTab.instance.positionRealComboBox.getText());
//            System.out.println("real: " + real);
        } catch (NumberFormatException e) {
            real = new DoubleDouble(RenderPanel.instance.center.r);
            PositionTab.instance.positionRealComboBox.setText(real.toString());
        }
        try {
            imag = new DoubleDouble(PositionTab.instance.positionImaginaryComboBox.getText());
//            System.out.println("imag: " + imag);
        } catch (NumberFormatException e) {
            imag = new DoubleDouble(RenderPanel.instance.center.i);
            PositionTab.instance.positionRealComboBox.setText(imag.toString());
        }
        return new Complex(real, imag);
    }
    public static void setCenter(Complex center) {
        PositionTab.instance.positionRealComboBox.setText(center.r.toString());
        PositionTab.instance.positionImaginaryComboBox.setText(center.i.toString());
    }
    public static ColorPalette getColorPalette() {
        return PaletteSelectComboBox.instance.paletteHashMap.get(PaletteSelectComboBox.instance.palette.getSelectedItem());
    }
    public static ThreadType getRenderingEngine() {
        return SettingsTab.instance.renderingEngineSelectComboBox.threadHashMap.get(SettingsTab.instance.renderingEngineSelectComboBox.comboBox.getSelectedItem());
    }
    public static ColorAlgorithm getColorAlgorithm() {
        switch (PaletteSelectComboBox.instance.algorithmHashMap.get(PaletteSelectComboBox.instance.algorithm.getSelectedItem())) {
            case "Simple": return new SimpleColorAlgorithm(getColorPalette());
            case "Logarithmic": return new LogarithmicColorAlgorithm(getColorPalette());
        }
        return null;
    }
}
