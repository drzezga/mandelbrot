package colors.palettes;

import java.awt.*;

public class LoopingColorPalette extends ColorPalette {

    Color colors[];

    public LoopingColorPalette(int length, int loops, Color... colors) {

        this.colors = new Color[length * loops];
        Color[] newColors = new Color[colors.length * loops]; // 8
        for (int i = 0; i < loops * colors.length; i++) { // 2X
            newColors[i] = colors[i % colors.length];
        }
        colors = newColors;
//        for (int i = 0; i < colors.length; i++) {
//            System.out.println(colors[i]);
//        }

        float step = (float)(this.colors.length - 1) / (float)(colors.length - 1);

        int prevIndex = 0;
        for (int i = 1; i < colors.length; i++) {
            int index = Math.round(i * step);
            Color[] lerped = lerp(colors[i-1], colors[i], index - prevIndex);
            for (int j = 0; j < lerped.length; j++) {
                this.colors[prevIndex + j] = lerped[j];
            }
            prevIndex = index;
        }
        if (this.colors[this.colors.length - 1] == null) {
            this.colors[this.colors.length - 1] = colors[colors.length - 1];
        }
    }

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
