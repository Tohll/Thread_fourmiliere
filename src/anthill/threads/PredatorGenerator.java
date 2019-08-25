package anthill.threads;

import java.util.Random;

import anthill.ants.Predator;
import anthill.controllers.Anthills;
import anthill.controllers.Creeps;

public class PredatorGenerator implements Runnable {

    private int predatorIndex;
    private final Random rand;

    public PredatorGenerator() {
        this.rand = new Random();
        this.predatorIndex = 1;
    }

    @Override
    public void run() {

        while (Anthills._getInstance()._getQueen()._getRunnable()._isAlive()) {
            try {
                Thread.sleep(this.rand.nextInt(7001) + 8000L);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            final RunnableHolder predator = new RunnableHolder(
                    new Predator(this.predatorIndex, 10, Anthills._getInstance()),
                    String.format("Predator %d", this.predatorIndex));
            Creeps._getInstance()._getPredators().add(predator);
            predator.start();
            this.predatorIndex++;
        }
    }
}