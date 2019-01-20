package colors.palettes;

import java.awt.*;

public class ExtendedConstantFillColorPalette extends ColorPalette {

    public ExtendedConstantFillColorPalette(int size, Color fillColor, Color... _colors) {
        colors = new Color[size];

        float step = (float)(colors.length - 1) / (float)(_colors.length - 1);


        int prevIndex = 0;
        for (int i = 1; i < _colors.length; i++) {
            int index = Math.round(i * step);
            Color[] lerped = ColorPalette.lerp(_colors[i-1], _colors[i], index - prevIndex);
            for (int j = 0; j < lerped.length; j++) {
                colors[prevIndex + j] = lerped[j];
            }
            prevIndex = index;
        }
        if (colors[colors.length - 1] == null) {
            System.out.println(_colors.length);
            colors[colors.length - 1] = _colors[_colors.length - 1];
        }
        colors[colors.length - 1] = fillColor;
    }
}
