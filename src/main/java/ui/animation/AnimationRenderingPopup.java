package ui.animation;

import javax.swing.*;

public class AnimationRenderingPopup extends JDialog {
    String[] encodingSpeedOptions = { "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "slower", "veryslow" };

    public AnimationRenderingPopup() {
//        super();
        this.setModal(true);
        this.setTitle("Animation rendering");

        this.add(new JLabel("Choose the animation settings"));


        this.add(new JLabel("Framerate"));
        this.add(new JTextField());

        this.add(new JLabel("Compressing speed"));
        this.add(new JComboBox<>(encodingSpeedOptions));
        this.add(new JLabel("Compressing quality highest to lowest"));
        this.add(new JSlider(0, 51));
        this.add(new JButton("Render"));
        this.add(new JProgressBar());
    }


}
