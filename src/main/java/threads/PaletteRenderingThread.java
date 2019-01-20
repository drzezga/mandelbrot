package threads;

import util.Complex;
import util.PixelRenderData;

public class PaletteRenderingThread extends RenderingThread {

    public PaletteRenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    @Override
    void calculate(PixelRenderData renderData) {
//        System.out.println(renderData.x);
        parent.setPixel(renderData.x, renderData.y, renderData.colorAlgorithm.calculate((float)renderData.x, new Complex(), renderData.w));
    }
}
