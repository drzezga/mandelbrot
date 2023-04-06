package colors.algorithms;

import colors.palettes.ColorPalette;
import misc.Complex;

import java.awt.*;

public class SimpleColorAlgorithm extends ColorAlgorithm {

    public SimpleColorAlgorithm(ColorPalette _palette) {
        palette = _palette;
    }

    @Override
    public Color calculate(float n, Complex z, int threshold, Complex zn) {
        int index = Math.round((n / (float)threshold * (float)(palette.colors.length - 1)));
        return palette.get(index);
    }
}
