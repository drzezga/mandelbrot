package ui.timeline;

import javax.swing.*;

public class KeyframeContextMenu extends JPopupMenu {
    public JMenuItem delButton;
    public JMenuItem propButton;
    public JMenuItem toggleButton;

    public KeyframeContextMenu(Keyframe kf) {
        propButton = new JMenuItem("Properties");
        delButton = new JMenuItem("Delete");
        toggleButton = new JMenuItem("Disable/Enable");

        add(propButton);
//        add(new JMenuBar());
        add(delButton);
        add(new JMenuItem("Clone"));
        add(toggleButton);
        add(new JPopupMenu.Separator());
        add(new JMenuItem("Set to current view"));
        add(new JMenuItem("Render in view"));
        add(new JPopupMenu.Separator());
        add(new JMenuItem("Position: " + kf.position));
    }
}
