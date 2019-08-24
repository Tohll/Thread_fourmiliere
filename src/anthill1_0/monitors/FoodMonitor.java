package anthill1_0.monitors;

import anthill1_0.ants.AbsAnt;

/**
 * @author Seldan
 *
 */
public class FoodMonitor {

    private boolean foodIsAccessible = true;
    private int foodStock;
    private final AbsAnt[] foodWaitingLine;

    private int headFood;
    private int tailFood;

    /**
     * Monitor to Handle a critical food stock. Has a FIFO waiting line build-in.
     *
     * @param foodStock Food quantity to initialize the foodStock with.
     * @param capacity  The waiting line capacity. Must be equal to the maximum of
     *                  object expected to wait on this monitor to avoid thread
     *                  issues.
     */
    public FoodMonitor(final int foodStock, final int capacity) {
        this.foodStock = foodStock;
        this.foodWaitingLine = new AbsAnt[capacity];
        this.headFood = 0;
        this.tailFood = 0;
    }

    public synchronized void _addFood(final int quantity) throws InterruptedException {
        while (!this.foodIsAccessible) {
            this.wait();
        }
        this.foodIsAccessible = false;
        this.foodStock += quantity;
        this._interactionDone();
    }

    /**
     * Synchronized method to access food value
     *
     * @param value How much food you want to take
     * @param ant   Which ant want to access food
     * @return The int quantity of food consumed by the ant
     * @throws InterruptedException in case of thread interrupt
     */
    public synchronized int _getFood(final int value, final AbsAnt ant) throws InterruptedException {
        while (!this.foodIsAccessible || !this.isFoodPrioritary(ant)) {
            if (this.isInWaitingFoodLine(ant)) {
                this.wait();
            } else {
                this.foodWaitingLine[this.headFood] = ant;
                this.headFood = (this.headFood + 1) % this.foodWaitingLine.length;
                this.wait();
            }
        }
        this.foodIsAccessible = false;
        if (this.foodWaitingLine[this.tailFood] != null) {
            this.foodWaitingLine[this.tailFood] = null;
            this.tailFood = (this.tailFood + 1) % this.foodWaitingLine.length;
        }
        int quantity = 0;
        if (this.foodStock - value > -1) {
            this.foodStock -= value;
            quantity = value;
        }
        return quantity;
    }

    public int _getFoodStock() {
        return this.foodStock < 1 ? 0 : this.foodStock;
    }

    public synchronized void _interactionDone() {
        this.foodIsAccessible = true;
        this.notifyAll();
    }

    /**
     * @param ant The ant to compare
     * @return true if ant is the FI, else false
     */
    private boolean isFoodPrioritary(final AbsAnt ant) {
        boolean result = false;
        if (this.foodWaitingLine[this.tailFood] == null || this.foodWaitingLine[this.tailFood] == ant) {
            result = true;
        }
        return result;
    }

    /**
     * @param ant The ant to compare
     * @return true if ant is already waiting in the line, else false
     */
    private boolean isInWaitingFoodLine(final AbsAnt ant) {
        boolean result = false;
        for (final AbsAnt waitingAnt : this.foodWaitingLine) {
            if (waitingAnt != null && waitingAnt == ant) {
                result = true;
            }
        }
        return result;
    }
}