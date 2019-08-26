package anthill.threads;

import anthill.ants.AbsCreep;

public class RunnableHolder extends Thread {

    private final AbsCreep runnable;

    public RunnableHolder(final AbsCreep runnable, final String name) {
        super(runnable, name);
        this.runnable = runnable;
    }

    public AbsCreep _getRunnable() {
        return this.runnable;
    }

}
