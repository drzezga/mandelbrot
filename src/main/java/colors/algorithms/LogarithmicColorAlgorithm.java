package colors.algorithms;

import colors.palettes.ColorPalette;
import util.Complex;

import java.awt.*;

public class LogarithmicColorAlgorithm extends ColorAlgorithm {
    public LogarithmicColorAlgorithm(ColorPalette _palette) {
        palette = _palette;
    }

    @Override
    public Color calculate(float n, Complex z, int threshold) {
        if (n == threshold) {
            return new Color(0);
        }
        double nu = n - Math.log(Math.log(n));
        int index = (int) (nu * 10) % palette.colors.length;

        return palette.get(index);
    }
}
