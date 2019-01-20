package colors.palettes;

import java.awt.*;

public class ColorPalette {

    public Color[] colors;

    public ColorPalette(Color... _colors) {
        colors = new Color[256];

        float step = (float)(colors.length - 1) / (float)(_colors.length - 1);

        int prevIndex = 0;
        for (int i = 1; i < _colors.length; i++) {
            int index = Math.round(i * step);
            Color[] lerped = lerp(_colors[i-1], _colors[i], index - prevIndex);
            for (int j = 0; j < lerped.length; j++) {
                colors[prevIndex + j] = lerped[j];
            }
            prevIndex = index;
        }
        // The last pixel can be sometimes null due to a rounding error.
        // This can be quickly fixed with a simple check.
        if (colors[colors.length - 1] == null) {
//            System.out.println(_colors.length);
            colors[colors.length - 1] = _colors[_colors.length - 1];
        }
    }
    public ColorPalette() {}

    public Color get(int i) {
        try {
            return colors[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Color(128, 128, 128);
        }
    }

    public static Color[] lerp(Color start, Color end, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("interpolate: illegal count!");
        }
        Color[] array = new Color[count + 1];
        for (int i = 0; i <= count; ++ i) {
            int r = start.getRed() + i * (end.getRed() - start.getRed()) / count;
            int g = start.getGreen() + i * (end.getGreen() - start.getGreen()) / count;
            int b = start.getBlue() + i * (end.getBlue() - start.getBlue()) / count;
            array[i] = new Color(r, g, b);
        }
        return array;
    }
}
