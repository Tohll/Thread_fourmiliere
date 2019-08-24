package anthill.ants;

import java.awt.Point;
import java.util.Random;

import anthill.controllers.Anthills;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public abstract class AbsAnt implements Runnable {

    protected Anthills anthill;
    private final int fuzzRate;
    protected int hunger;
    protected boolean isAlive;
    protected boolean isUnderground;
    protected int life;
    protected String name;
    protected int nbrOfMealsTaken;
    protected int number;
    protected Point position;
    protected Random rand;
    /**
     * The lower the faster. Can't be lower than 1 : Any value lower than 1 will
     * define a speedIndex at 1
     */
    private final long speedIndex;
    protected Point target;

    /**
     * Configuration Ant builder.
     *
     * @param number     : will be used to compose the name of the ant.
     * @param life       : How much life point an ant has.
     * @param anthill    : to which antHill this ant is associated
     * @param speedIndex : the ant's speed : the lower the faster. Every value lower
     *                   than 1 will be brought back to 1.
     */
    protected AbsAnt(final int number, final int life, final Anthills anthill, final int speedIndex) {
        this.fuzzRate = 2;
        this.life = life;
        this.isUnderground = true;
        this.number = number;
        this.anthill = anthill;
        this.nbrOfMealsTaken = 0;
        this.position = new Point(Configuration.SQUARE_SIDE / 2, Configuration.SQUARE_SIDE / 2);
        this.target = new Point(Configuration.SQUARE_SIDE / 2, Configuration.SQUARE_SIDE / 2);
        this.speedIndex = speedIndex < 1 ? 1 : speedIndex;
        this.isAlive = true;
        this.hunger = 0;
        this.rand = new Random();
        this.name = String.format("Unnamed %d", number);
    }

    public String _getName() {
        return this.name;
    }

    public int _getNbrRepas() {
        return this.nbrOfMealsTaken;
    }

    public Point _getPosition() {
        return this.position;
    }

    public boolean _isAlive() {
        return this.isAlive;
    }

    public boolean _isUnderground() {
        return this.isUnderground;
    }

    /**
     * The Configuration logic of a single ant
     */
    protected abstract void act();

    /**
     * Movement logic of any ant/insect
     * 
     * @param isFuzzy If true, the movements of the creature will be lighly random.
     * @throws InterruptedException
     */
    protected void move(final boolean isFuzzy) throws InterruptedException {
        if (isFuzzy) {
            while (this.position.x != this.target.x || this.position.y != this.target.y) {
                if (this.position.x >= this.target.x) {
                    this.position.x = this.position.x - (this.rand.nextInt(this.fuzzRate) + 2);
                } else if (this.position.x <= this.target.x) {
                    this.position.x = this.position.x + (this.rand.nextInt(this.fuzzRate) + 2);
                }
                if (this.position.y >= this.target.y) {
                    this.position.y = this.position.y - (this.rand.nextInt(this.fuzzRate) + 2);
                } else if (this.position.y <= this.target.y) {
                    this.position.y = this.position.y + (this.rand.nextInt(this.fuzzRate) + 2);
                }
                Thread.sleep(this.speedIndex * 3);
            }
        } else {
            while (this.position.x != this.target.x || this.position.y != this.target.y) {
                if (this.position.x > this.target.x) {
                    this.position.x = this.position.x - 1;
                } else if (this.position.x < this.target.x) {
                    this.position.x = this.position.x + 1;
                }
                if (this.position.y > this.target.y) {
                    this.position.y = this.position.y - 1;
                } else if (this.position.y < this.target.y) {
                    this.position.y = this.position.y + 1;
                }
                Thread.sleep(this.speedIndex * 3);
            }
        }
    }

    @Override
    public void run() {
        while (this.life > 0 && !Thread.currentThread().isInterrupted()) {
            this.act();
        }
        this.isAlive = false;
    }
}
