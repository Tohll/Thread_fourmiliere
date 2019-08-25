package anthill.threads;

import java.util.ArrayList;
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

    private void cleanPredators() {
        final ArrayList<RunnableHolder> deadPredators = new ArrayList<>();
        for (final RunnableHolder deadPredator : Creeps._getInstance()._getPredators()) {
            if (!deadPredator.isAlive()) {
                deadPredators.add(deadPredator);
            }
        }
        if (!deadPredators.isEmpty()) {
            Creeps._getInstance()._getPredators().removeAll(deadPredators);
        }
        System.out.printf("Predators size : %d%n", Creeps._getInstance()._getPredators().size());
    }

    private void generateOnePredator() {
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
        this.predatorIndex++;
        predator.start();
    }

    @Override
    public void run() {

        while (Anthills._getInstance()._getQueen()._getRunnable()._isAlive()) {
            this.generateOnePredator();
            this.cleanPredators();
        }
    }
}