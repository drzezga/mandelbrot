package ui;

import ui.tabs.PositionTab;
import ui.tabs.RenderingTab;
import ui.tabs.SettingsTab;
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
                    PositionTab.instance.zoomComboBox.setText( Double.toString(renderPanel.renderData.zoom * 2));
                    renderPanel.render();
                    break;
                case 70: // f - scale out
                    PositionTab.instance.zoomComboBox.setText( Double.toString(renderPanel.renderData.zoom / 2));
                    renderPanel.render();
                    break;
                case 90: // z - threshold -= 50
                    if (renderPanel.renderData.threshold > 50)
                        VariablesTab.instance.threshold.setValue( renderPanel.renderData.threshold - 50);
                    System.out.println(renderPanel.renderData.threshold);
                    renderPanel.render();
                    break;
                case 88: // x - threshold += 50
                    VariablesTab.instance.threshold.setValue( renderPanel.renderData.threshold + 50);
                    System.out.println(renderPanel.renderData.threshold);
                    renderPanel.render();
                    break;
                case 81: // q - render
                    renderPanel.render();
                    break;
                case 89: // y - toggle julia/double
                    if (SettingsTab.instance.renderingEngineSelectComboBox.comboBox.getSelectedItem() == "Julia Set") {
                        SettingsTab.instance.renderingEngineSelectComboBox.comboBox.setSelectedItem("Double");
                    } else {
                        SettingsTab.instance.renderingEngineSelectComboBox.comboBox.setSelectedItem("Julia Set");
                    }
                    renderPanel.render();
                    break;
            }
        }
    }
}
