package ui.animation;

import javax.swing.*;

public class KeyframeContextMenu extends JPopupMenu {
    public JMenuItem delButton;
    public JMenuItem propButton;
    public JMenuItem cloneButton;
    public JMenuItem toggleButton;
    public JMenuItem setCurrentViewButton;
    public JMenuItem setCurrentViewToThisButton;

    public KeyframeContextMenu(Keyframe kf) {
        propButton = new JMenuItem("Properties");
        delButton = new JMenuItem("Delete");
        cloneButton = new JMenuItem("Clone");
        toggleButton = new JMenuItem("Disable/Enable");
        setCurrentViewButton = new JMenuItem("Set");
        setCurrentViewToThisButton = new JMenuItem("Go");

        add(propButton);
        if (!(kf instanceof ImmutableKeyframe)) {
            add(delButton);
        }
        add(cloneButton);
        add(toggleButton);
        add(new JPopupMenu.Separator());
        add(setCurrentViewButton);
        add(setCurrentViewToThisButton);
        add(new JPopupMenu.Separator());
        add(new JMenuItem("Position: " + kf.getPosition()));
    }
}
