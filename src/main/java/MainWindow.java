import ui.TabPanel;
import ui.KeyHandler;
import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

    JPanel p = new JPanel();
    JPanel viewport = new RenderPanel();
    TabPanel tabPanel = new TabPanel();

    public static MainWindow instance;

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException | IllegalAccessException ignored) {}
        instance = new MainWindow();
    }

    public MainWindow() {
        super("Mandelbrot");
        setSize(800, 1000);
        setMinimumSize(new Dimension(800, 600));
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        p.setLayout(new BorderLayout());
        p.add(viewport, BorderLayout.CENTER);
        p.add(tabPanel, BorderLayout.PAGE_END);

        KeyHandler handler = new KeyHandler();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .addKeyEventDispatcher(new KeyEventDispatcher() {
                            @Override
                            public boolean dispatchKeyEvent(KeyEvent e) {
                                if (e.getID() == KeyEvent.KEY_RELEASED) {
                                    handler.keyReleased(e);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

        add(p);

        pack();
        setVisible(true);
    }
}
