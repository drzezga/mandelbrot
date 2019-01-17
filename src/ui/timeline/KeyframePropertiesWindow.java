package ui.timeline;

import javax.naming.Context;
import javax.swing.*;
import java.awt.*;

public class KeyframePropertiesWindow extends JFrame {
    Keyframe kf;

    public KeyframePropertiesWindow(Keyframe kf) {
        super("Keyframe properties");
        this.kf = kf;
        Container contextPane = getContentPane();

        contextPane.add(new JLabel("Keyframe properties"));
//        contextPane.add(new JLabel("Pos "));

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
