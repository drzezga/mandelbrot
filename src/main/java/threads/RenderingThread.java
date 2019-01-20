package threads;

import util.PixelRenderData;
import util.ThreadStallData;

abstract public class RenderingThread extends Thread {

    public RenderingManagerThread parent;

    private boolean finished = false;

//    public RenderingThread(RenderingManagerThread parent) {
//        this.parent = parent;
//    }

    public RenderingThread() {}

    public void run() {
        while (!finished) {
            PixelRenderData data = parent.fetchData();
            if (data != null && data.getClass() == ThreadStallData.class) {
                try {
                    synchronized (this) {
                        wait(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (data != null) {
                calculate(data);
                if (data.last) {
                    if (parent instanceof AnimationRenderingThread) {
                        AnimationRenderingThread animParent = (AnimationRenderingThread) parent;
                        animParent.nextFrame();
                    }
                }
            } else {
                finished = true;
            }
        }
    }

    public boolean isShiftRenderingEnabled() {
        return true;
    }

    abstract void calculate(PixelRenderData renderData);
}
