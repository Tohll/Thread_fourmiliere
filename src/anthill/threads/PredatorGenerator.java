package anthill.threads;

import java.util.Random;

import anthill.ants.Predator;
import anthill.controllers.Anthills;
import anthill.controllers.Creeps;

public class PredatorGenerator implements Runnable {

    private Random rand;
    private int predatorIndex;
    
    public PredatorGenerator() {
        this.rand = new Random();
        this.predatorIndex = 1;
    }

    @Override
    public void run() {

        while (Anthills._getInstance()._getQueen()._isAlive()) {
            try {
                Thread.sleep(this.rand.nextInt(7001) + 8000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            
            RunnableHolder predator = new RunnableHolder(new Predator(predatorIndex, 10, Anthills._getInstance()), String.format("Predator %d", predatorIndex));
            Creeps._getInstance()._getPredators().add(predator);
            predator.start();
            predatorIndex++;
        }
    }
}