package colors.algorithms;

import colors.palettes.ColorPalette;
import util.Complex;

import java.awt.*;

public class SmoothColorAlgorithm extends ColorAlgorithm {
    public SmoothColorAlgorithm(ColorPalette _palette) {
        palette = _palette;
    }

    @Override
    public Color calculate(float n, Complex z, int threshold) {
        if (n == threshold) {
            return new Color(0);
        }
        double nu = n + 1 - (Math.log(Math.log(z.abs().doubleValue())) / Math.log(2));
//        double nu = n - Math.log(Math.log(n));
        int index = (int) nu % palette.colors.length;
//        return Color.getHSBColor(0.95f + 10 * (float) nu,0.6f,1.0f);

        return palette.get(index);
    }
}
