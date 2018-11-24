package ui.tabs;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class VariablesTab extends JPanel {
    public static VariablesTab instance;

    public JFormattedTextField threshold = new JFormattedTextField(NumberFormat.getIntegerInstance());

    public VariablesTab() {
        instance = this;

        threshold.setValue(200);

        threshold.setPreferredSize(new Dimension(60, 21));

        add(new JLabel("Threshold"));
        add(threshold);
    }
}
