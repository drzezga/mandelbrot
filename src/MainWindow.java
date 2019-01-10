import ui.TabPanel;
import ui.KeyHandler;
import ui.RenderPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    JPanel p = new JPanel();
    JPanel viewport = new RenderPanel();
    TabPanel tabPanel = new TabPanel();

    public static MainWindow instance;

    public static void main(String[] args) {
        try {
            // Set System L&F
            if (UIManager.getSystemLookAndFeelClassName() != "com.sun.java.swing.plaf.motif.MotifLookAndFeel") {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {}
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
        tabPanel.addKeyListener(new KeyHandler());
        add(p);

        pack();
        setVisible(true);
    }
}
