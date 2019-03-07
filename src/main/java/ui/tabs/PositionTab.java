package ui.tabs;

import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;

public class PositionTab extends JPanel {

    public static PositionTab instance;

    public JTextField positionRealComboBox;
    public JTextField positionImaginaryComboBox;
    public JTextField zoomComboBox;

    public PositionTab() {
        instance = this;
        positionRealComboBox = new JTextField("-0.75");
        positionImaginaryComboBox = new JTextField("0");
        zoomComboBox = new JTextField("1");

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
        labelPanel.add(new JLabel("Zoom", SwingConstants.CENTER), BorderLayout.PAGE_END);

        textBoxPanel.add(positionRealComboBox, BorderLayout.PAGE_START);
        textBoxPanel.add(positionImaginaryComboBox, BorderLayout.CENTER);
        textBoxPanel.add(zoomComboBox, BorderLayout.PAGE_END);

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
            zoomComboBox.setText("1");
            RenderPanel.instance.render();
        });
    }
}
