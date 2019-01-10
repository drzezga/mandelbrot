package util;

import colors.algorithms.ColorAlgorithm;
import colors.algorithms.LogarithmicColorAlgorithm;
import colors.algorithms.SimpleColorAlgorithm;
import colors.algorithms.SmoothColorAlgorithm;
import colors.palettes.ColorPalette;
import threads.ThreadType;
import ui.RenderPanel;
import ui.components.PaletteSelectComboBox;
import ui.components.ResolutionTextField;
import ui.tabs.PositionTab;
import ui.tabs.SettingsTab;
import ui.tabs.VariablesTab;

import java.math.BigDecimal;

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
        PositionTab.instance.scaleComboBox.setText(Double.toString(scale));
    }

    public static Complex getCenter() {
        BigDecimal real;
        BigDecimal imag;
        try {
            real = new BigDecimal(PositionTab.instance.positionRealComboBox.getText());
        } catch (NumberFormatException e) {
            real = RenderPanel.instance.center.r;
            PositionTab.instance.positionRealComboBox.setText(real.toPlainString());
        }
        try {
            imag = new BigDecimal(PositionTab.instance.positionImaginaryComboBox.getText());
        } catch (NumberFormatException e) {
            imag = RenderPanel.instance.center.i;
            PositionTab.instance.positionRealComboBox.setText(imag.toPlainString());
        }
        return new Complex(real, imag);
    }

    public static void setCenter(Complex center) {
        PositionTab.instance.positionRealComboBox.setText(center.r.stripTrailingZeros().toEngineeringString());
        PositionTab.instance.positionImaginaryComboBox.setText(center.i.stripTrailingZeros().toEngineeringString());
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
            case "Smooth": return new SmoothColorAlgorithm(getColorPalette());
        }
        return null;
    }
}
