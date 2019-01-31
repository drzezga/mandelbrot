package colors.algorithms;

import colors.palettes.ColorPalette;
import misc.Complex;

import java.awt.*;

public abstract class ColorAlgorithm {

    ColorPalette palette;

    public ColorAlgorithm() {}
    public ColorAlgorithm(ColorPalette _palette) {
        palette = _palette;
    }

    public abstract Color calculate(float n, Complex z, int threshold);
}
