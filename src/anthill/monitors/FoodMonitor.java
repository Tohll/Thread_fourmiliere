package anthill.monitors;

import anthill.ants.AbsCreep;

/**
 * @author Seldan
 *
 */
public class FoodMonitor {

    private boolean foodIsAccessible = true;
    private int foodStock;
    private final AbsCreep[] foodWaitingLine;

    private int headFood;
    private int size;
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
        this.foodWaitingLine = new AbsCreep[capacity];
        this.headFood = 0;
        this.tailFood = 0;
        this.size = 0;
    }

    /**
     * Synchronized method to access food value
     *
     * @param value      How much food you want to take
     * @param ant        Which ant want to access food
     * @param isDeposing is the ant deposing food rather than picking up ?
     * @return The int quantity of food consumed by the ant
     * @throws InterruptedException in case of thread interrupt
     */
    public synchronized int _accesFood(final int value, final AbsCreep ant, final boolean isDeposing)
            throws InterruptedException {
        while (!this.foodIsAccessible || !this.isFoodPrioritary(ant)) {
            if (this.isInWaitingFoodLine(ant)) {
                this.wait();
            } else {
                this.foodWaitingLine[this.headFood] = ant;
                this.headFood = (this.headFood + 1) % this.foodWaitingLine.length;
                this.size++;
                this.wait();
            }
        }
        this.foodIsAccessible = false;
        if (this.foodWaitingLine[this.tailFood] != null) {
            this.foodWaitingLine[this.tailFood] = null;
            this.tailFood = (this.tailFood + 1) % this.foodWaitingLine.length;
            this.size--;
        }
        int quantity = 0;
        if (isDeposing) {
            this.foodStock += value;
        } else {
            if (this.foodStock - value > -1) {
                this.foodStock -= value;
                quantity = value;
            }
        }
        return quantity;
    }

    public int _getFoodStock() {
        return this.foodStock < 1 ? 0 : this.foodStock;
    }

    public int _getSize() {
        return this.size;
    }

    public synchronized void _interactionDone() {
        this.foodIsAccessible = true;
        this.notifyAll();
    }

    /**
     * @param ant The ant to compare
     * @return true if ant is the FI, else false
     */
    private boolean isFoodPrioritary(final AbsCreep ant) {
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
    private boolean isInWaitingFoodLine(final AbsCreep ant) {
        boolean result = false;
        for (final AbsCreep waitingAnt : this.foodWaitingLine) {
            if (waitingAnt != null && waitingAnt == ant) {
                result = true;
            }
        }
        return result;
    }
}