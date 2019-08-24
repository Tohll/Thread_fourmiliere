package anthill1_0.threads;

import java.util.ArrayList;
import java.util.List;

public class AntsInitiator implements Runnable {

    private final ArrayList<RunnableHolder> antsArray;

    public AntsInitiator(final List<RunnableHolder> antsArray) {
        this.antsArray = (ArrayList<RunnableHolder>) antsArray;
    }

    @Override
    public void run() {

        try {
            for (final RunnableHolder runnableHolder : this.antsArray) {
                runnableHolder.start();
                Thread.sleep(1);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
