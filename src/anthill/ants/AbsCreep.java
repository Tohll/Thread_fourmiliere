package anthill.ants;

import java.awt.Point;
import java.util.Random;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public abstract class AbsCreep implements Runnable {

    protected Anthills anthill;
    private final int fuzzRate;
    protected int hunger;
    protected boolean isAlive;
    protected boolean isUnderground;
    protected int life;
    protected int maxLife;
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
    protected AbsCreep(final int number, final int life, final Anthills anthill, final int speedIndex) {
        this.fuzzRate = 4;
        this.life = life;
        this.maxLife = life;
        this.isUnderground = true;
        this.number = number;
        this.anthill = anthill;
        this.nbrOfMealsTaken = 0;
        this.position = new Point(Configuration.WIDTH / 2, Configuration.HEIGHT / 2);
        this.target = new Point(Configuration.WIDTH / 2, Configuration.HEIGHT / 2);
        this.speedIndex = speedIndex < 1 ? 1 : speedIndex;
        this.isAlive = true;
        this.hunger = 0;
        this.rand = new Random();
        this.name = String.format("Unnamed %d", number);
    }

    public int _getLife() {
        return this.life < 0 ? 0 : this.life;
    }

    public int _getMaxLife() {
        return this.maxLife;
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

    public void _receiveDamage(final int damageInflicted) {
        this.life = this.life - damageInflicted;
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
            while ((this.position.x != this.target.x || this.position.y != this.target.y) && this.life > 0) {
                if (Collisions._getInstance().isClose(this.position, this.target, this.fuzzRate)) {
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
                    Thread.sleep(this.speedIndex * 2);
                } else {
                    final Point tempTarget = new Point();

                    tempTarget.x = this.target.x + (this.fuzzRate * (this.rand.nextBoolean() ? 1 : -1));
                    tempTarget.y = this.target.y + (this.fuzzRate * (this.rand.nextBoolean() ? 1 : -1));

                    if ((this.position.x > tempTarget.x && this.position.y > tempTarget.y)
                            || (this.position.x < tempTarget.x && this.position.y < tempTarget.y)
                            || (this.position.x > tempTarget.x && this.position.y < tempTarget.y)
                            || (this.position.x < tempTarget.x && this.position.y > tempTarget.y)) {
                        if (this.rand.nextBoolean()) {
                            if (this.position.x >= tempTarget.x) {
                                this.position.x = this.position.x - 1;
                            } else if (this.position.x <= tempTarget.x) {
                                this.position.x = this.position.x + 1;
                            }
                            Thread.sleep(this.speedIndex * 2);
                        } else {
                            if (this.position.y >= tempTarget.y) {
                                this.position.y = this.position.y - 1;
                            } else if (this.position.y <= tempTarget.y) {
                                this.position.y = this.position.y + 1;
                            }
                            Thread.sleep(this.speedIndex * 2);
                        }
                    } else {
                        if (this.position.x >= tempTarget.x) {
                            this.position.x = this.position.x - 2;
                        } else if (this.position.x <= tempTarget.x) {
                            this.position.x = this.position.x + 2;
                        }
                        if (this.position.y >= tempTarget.y) {
                            this.position.y = this.position.y - 2;
                        } else if (this.position.y <= tempTarget.y) {
                            this.position.y = this.position.y + 2;
                        }
                        Thread.sleep(this.speedIndex * 2);
                    }
                }
            }
        } else {
            while ((this.position.x != this.target.x || this.position.y != this.target.y) && this.life > 0) {
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

    protected void moveToCloseRandomPoint(final int maxDistance) {
        int value = this.rand.nextInt(maxDistance) + 1;
        if (this.rand.nextBoolean()) {
            this.target.x = this.target.x + value;
            if (this.target.x > Configuration.WIDTH) {
                this.target.x = Configuration.WIDTH;
            }
        } else {
            this.target.x = this.target.x - value;
            if (this.target.x < 0) {
                this.target.x = 0;
            }
        }
        value = this.rand.nextInt(50) + 1;
        if (this.rand.nextBoolean()) {
            this.target.y = this.target.y + value;
            if (this.target.y > Configuration.WIDTH) {
                this.target.y = Configuration.WIDTH;
            }
        } else {
            this.target.y = this.target.y - value;
            if (this.target.y < 0) {
                this.target.y = 0;
            }
        }
        try {
            this.move(true);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
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
