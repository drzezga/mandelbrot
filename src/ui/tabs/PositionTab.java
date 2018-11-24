package ui.tabs;


import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;

public class PositionTab extends JPanel {

    public static PositionTab instance;

    public JFormattedTextField positionRealComboBox;
    public JFormattedTextField positionImaginaryComboBox;
    public JFormattedTextField scaleComboBox;

    public PositionTab() {
        instance = this;
        positionRealComboBox = new JFormattedTextField(-0.75);
        positionImaginaryComboBox = new JFormattedTextField(0);
        scaleComboBox = new JFormattedTextField(3);

        JButton resetPosButton = new JButton("Reset position");
        JButton resetScaleButton = new JButton("Reset scale");

        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        JPanel textBoxPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        labelPanel.setLayout(new BorderLayout());
        textBoxPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());

        labelPanel.add(new JLabel("Real", SwingConstants.CENTER), BorderLayout.PAGE_START);
        labelPanel.add(new JLabel(" Imaginary ", SwingConstants.CENTER), BorderLayout.CENTER);
        labelPanel.add(new JLabel("Scale", SwingConstants.CENTER), BorderLayout.PAGE_END);

        textBoxPanel.add(positionRealComboBox, BorderLayout.PAGE_START);
        textBoxPanel.add(positionImaginaryComboBox, BorderLayout.CENTER);
        textBoxPanel.add(scaleComboBox, BorderLayout.PAGE_END);

        buttonPanel.add(resetPosButton, BorderLayout.CENTER);
        buttonPanel.add(resetScaleButton, BorderLayout.PAGE_END);

        add(labelPanel, BorderLayout.LINE_START);
        add(textBoxPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.LINE_END);

        resetPosButton.addActionListener(e -> {
            positionRealComboBox.setText("-0.75");
            positionImaginaryComboBox.setText("0");
            RenderPanel.instance.render();
        });
        resetScaleButton.addActionListener(e -> {
            scaleComboBox.setText("3");
            RenderPanel.instance.render();
        });
    }
}
