package ui.timeline;

import javax.swing.*;
import java.awt.*;

public class KeyframePropertiesWindow extends JFrame {
    Keyframe kf;

    public KeyframePropertiesWindow(Keyframe kf) {
        super("Keyframe properties");
        this.kf = kf;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(new JLabel("Keyframe properties"));
        panel.add(new JLabel("Pos Real: " + kf.getRenderData().center.r.toString()));
        panel.add(new JLabel("Pos Imag: " + kf.getRenderData().center.i.toString()));
        panel.add(new JLabel("Scale: " + kf.getRenderData().scale));
        panel.add(new JLabel("Threshold: " + kf.getRenderData().threshold));
        panel.add(new JLabel("Z power: " + kf.getRenderData().zPow));

        getContentPane().add(panel);
        pack();
        setMinimumSize(new Dimension(300, 400));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        // TODO: Add:
        //  - Interpolation type
        //  - Position
        //  - Threshold
        //  - Scale
        //  - Power of z
        //  -
    }
}
