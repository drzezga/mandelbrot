package ui.animation;

import javax.swing.*;
import java.awt.*;

public class KeyframePropertiesWindow extends JFrame {
//    Keyframe kf;

    private JComboBox<Keyframe.InterpolationType> interpolationTypeComboBox;

    public KeyframePropertiesWindow(Keyframe kf) {
        super("Keyframe properties");
//        this.kf = kf;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(new JLabel("Keyframe properties"));
        panel.add(new JLabel("Pos Real: " + kf.getRenderData().center.r.toString()));
        panel.add(new JLabel("Pos Imag: " + kf.getRenderData().center.i.toString()));
        panel.add(new JLabel("Zoom: " + kf.getRenderData().zoom));
        panel.add(new JLabel("Threshold: " + kf.getRenderData().threshold));
        panel.add(new JLabel("Z power: " + kf.getRenderData().zPow));
        panel.add(new JLabel("Interpolation type:"));

        interpolationTypeComboBox = new JComboBox<>(Keyframe.InterpolationType.values());
        interpolationTypeComboBox.setSelectedItem(kf.getInterpolationType());
        interpolationTypeComboBox.addActionListener(ae -> kf.setInterpolationType((Keyframe.InterpolationType) interpolationTypeComboBox.getSelectedItem()));
//        interpolationTypeComboBox.setPreferredSize(new Dimension(100, 25));

        panel.add(interpolationTypeComboBox);

        getContentPane().add(panel);
        pack();
        setMinimumSize(new Dimension(150, 200));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
