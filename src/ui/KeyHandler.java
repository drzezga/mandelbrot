package ui;

import ui.tabs.PositionTab;
import ui.tabs.RenderingTab;
import ui.tabs.VariablesTab;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static KeyHandler instance;

    private RenderPanel renderPanel;
    private RenderingTab renderingTab;

    public KeyHandler() {
        instance = this;

        renderPanel = RenderPanel.instance;
        renderingTab = RenderingTab.instance;
    }

    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {
//        System.out.println(e.getKeyCode());
        if (!renderingTab.rendering) {
            switch (e.getKeyCode()) {
                case 87: // w - up
                    renderPanel.moveUp(40);
                    break;
                case 83: // s - down
                    renderPanel.moveDown(40);
                    break;
                case 65: // a - left
                    renderPanel.moveLeft(40);
                    break;
                case 68: // d - right
                    renderPanel.moveRight(40);
                    break;
                case 82: // r - scale in
                    PositionTab.instance.scaleComboBox.setText( Double.toString(renderPanel.scale / 2));
                    renderPanel.render();
                    break;
                case 70: // f - scale out
                    PositionTab.instance.scaleComboBox.setText( Double.toString(renderPanel.scale * 2));
                    renderPanel.render();
                    break;
                case 90: // z - threshold -= 50
                    if (renderPanel.threshold > 50)
                        VariablesTab.instance.threshold.setValue( renderPanel.threshold - 50);
                    System.out.println(renderPanel.threshold);
                    renderPanel.render();
                    break;
                case 88: // x - threshold += 50
                    VariablesTab.instance.threshold.setValue( renderPanel.threshold + 50);
                    System.out.println(renderPanel.threshold);
                    renderPanel.render();
                    break;
                case 81:
                    renderPanel.render();
                    break;
            }
        }
    }
}
