package colors.palettes;

import java.awt.*;

public class HSBColorPalette extends ColorPalette {

    public HSBColorPalette() {
        colors = new Color[256];

        for (int i = 0; i < colors.length; i++) {
            float val = ((float)1 / (float)colors.length) * i;
            colors[i] = Color.getHSBColor(val, 1, 1);
        }
    }

}
