package colors.algorithms;

import colors.palettes.ColorPalette;
import util.Complex;

import java.awt.*;

public class SimpleColorAlgorithm extends ColorAlgorithm {

    public SimpleColorAlgorithm(ColorPalette _palette) {
        palette = _palette;
    }

    @Override
    public Color calculate(float n, Complex z, int threshold) {
        int index = Math.round((n / (float)threshold * (float)(palette.colors.length - 1)));
        return palette.get(index);
    }
}
