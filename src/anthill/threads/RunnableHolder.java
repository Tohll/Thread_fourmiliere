package anthill.threads;

import anthill.ants.AbsAnt;

public class RunnableHolder extends Thread {

    private final AbsAnt runnable;

    public RunnableHolder(final AbsAnt runnable, final String name) {
        super(runnable, name);
        this.runnable = runnable;
    }

    public AbsAnt _getRunnable() {
        return this.runnable;
    }

}
