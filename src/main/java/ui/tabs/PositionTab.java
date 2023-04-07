package ui.tabs;

import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;

public class PositionTab extends JPanel {

    public static PositionTab instance;

    public JTextField threshold = new JTextField("200");
    public JTextField positionRealComboBox;
    public JTextField positionImaginaryComboBox;
    public JTextField zoomComboBox;

    public PositionTab() {
        instance = this;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        JPanel zoomPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());
        middlePanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new BorderLayout());
        zoomPanel.setLayout(new BorderLayout());

        // Labels
        JLabel realLabel = new JLabel("Real", SwingConstants.CENTER);
        JLabel imaginaryLabel = new JLabel("Imaginary", SwingConstants.CENTER);
        JLabel zoomLabel = new JLabel("Zoom", SwingConstants.CENTER);

        realLabel.setPreferredSize(new Dimension(50, 0));
        imaginaryLabel.setPreferredSize(new Dimension(50, 0));
        zoomLabel.setPreferredSize(new Dimension(50, 0));

        topPanel.add(realLabel, BorderLayout.LINE_START);
        middlePanel.add(imaginaryLabel, BorderLayout.LINE_START);
        zoomPanel.add(zoomLabel, BorderLayout.LINE_START);

        // Input boxes
        positionRealComboBox = new JTextField("-0.75");
        positionImaginaryComboBox = new JTextField("0");
        zoomComboBox = new JTextField("1");

        topPanel.add(positionRealComboBox, BorderLayout.CENTER);
        middlePanel.add(positionImaginaryComboBox, BorderLayout.CENTER);
        bottomPanel.add(zoomComboBox, BorderLayout.CENTER);

        // Reset buttons
        JButton resetRealButton = new JButton("Reset");
        JButton resetImButton = new JButton("Reset");
        JButton resetScaleButton = new JButton("Reset");

        topPanel.add(resetRealButton, BorderLayout.LINE_END);
        middlePanel.add(resetImButton, BorderLayout.LINE_END);
        zoomPanel.add(resetScaleButton, BorderLayout.LINE_END);

        bottomPanel.add(zoomPanel, BorderLayout.LINE_START);

        // Event listeners
        resetRealButton.addActionListener(e -> {
            positionRealComboBox.setText("-0.75");
//            positionImaginaryComboBox.setText("0");
            RenderPanel.instance.render();
        });
        resetImButton.addActionListener(e -> {
//            positionRealComboBox.setText("-0.75");
            positionImaginaryComboBox.setText("0");
            RenderPanel.instance.render();
        });
        resetScaleButton.addActionListener(e -> {
            zoomComboBox.setText("1");
            RenderPanel.instance.render();
        });

        JPanel thresholdPanel = new JPanel();
        thresholdPanel.setLayout(new BorderLayout());

        threshold.setPreferredSize(new Dimension(60, 21));

        thresholdPanel.add(new JLabel("Threshold"), BorderLayout.CENTER);
        thresholdPanel.add(threshold, BorderLayout.LINE_END);

        bottomPanel.add(thresholdPanel, BorderLayout.LINE_END);

        // All panels
        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
}
