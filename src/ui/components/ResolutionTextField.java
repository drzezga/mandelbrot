package ui.components;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class ResolutionTextField extends JPanel {

    public static ResolutionTextField instance;

    public JFormattedTextField xTextField;
    public JFormattedTextField yTextField;

    public ResolutionTextField() {
        instance = this;

        JLabel xLabel = new JLabel("X Resolution ");
        xTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());

        JLabel yLabel = new JLabel("Y Resolution ");
        yTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());

        xTextField.setValue(800);

        yTextField.setValue(800);

        setLayout(new GridLayout(2, 2));
        add(xLabel);
        add(xTextField);
        add(yLabel);
        add(yTextField);
    }
}
