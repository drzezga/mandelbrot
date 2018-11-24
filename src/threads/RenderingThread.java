package threads;

import util.RenderData;

abstract public class RenderingThread extends Thread {

    public RenderingManagerThread parent;

    private boolean finished = false;

    public RenderingThread(RenderingManagerThread parent) {
        this.parent = parent;
    }

    public RenderingThread() {}

    public void run() {
        while (!finished) {
            RenderData data = parent.fetchData();
            if (data != null) {
                calculate(data);
            } else {
                finished = true;
            }
        }
    }


    abstract void calculate(RenderData renderData);
}
