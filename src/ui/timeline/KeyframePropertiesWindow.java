package ui.timeline;

import javax.swing.*;
import java.awt.*;

public class KeyframePropertiesWindow extends JFrame {
    Keyframe kf;

    public KeyframePropertiesWindow(Keyframe kf) {
        super("Keyframe properties");
        this.kf = kf;

        getContentPane().add(new JLabel("Keyframe properties"));
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
