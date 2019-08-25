package anthill.ants;

import java.awt.Point;
import java.util.ArrayList;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.controllers.Creeps;
import anthill.controllers.FoodSpots;
import anthill.food_spots.FoodSpot;
import anthill.threads.RunnableHolder;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public final class Soldier extends AbsAnt {

    private RunnableHolder targetedPredator;

    public Soldier(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 5);
        this.name = "Soldier " + number;
        this.targetedPredator = null;
    }

    @Override
    protected void act() {
        if (this.life > 0 && this.hunger < 10) {
            this.choosePatrolTarget();
        }
        if (this.life > 0 && this.hunger < 10) {
            this.patroll();
        }
        if (this.life > 0) {
            this.comeback();
        }
        if (this.life > 0) {
            this.eat();
        }
    }

    private boolean checkForPredator() {
        boolean predatorFound = false;
        final ArrayList<RunnableHolder> predators = (ArrayList<RunnableHolder>) Creeps._getInstance()._getPredators();
        final ArrayList<RunnableHolder> activesPredators = new ArrayList<>();
        for (final RunnableHolder predator : predators) {
            if (predator.isAlive()) {
                activesPredators.add(predator);
            }
        }
        if (!activesPredators.isEmpty()) {
            predatorFound = true;
            if (activesPredators.size() == 1) {
                this.targetedPredator = activesPredators.get(0);
            } else {
                this.targetedPredator = activesPredators.get(this.rand.nextInt(activesPredators.size()));
            }
            this.target = this.targetedPredator._getRunnable()._getPosition();
            try {
                this.move(false);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.targetedPredator = null;
            this.target = new Point();
        }
        return predatorFound;
    }

    private void choosePatrolTarget() {
        this.checkForPredator();
        this.target.x = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
        this.target.y = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
    }

    private void comeback() {
        try {
            this.target.x = this.anthill._getPosition().x;
            this.target.y = this.anthill._getPosition().y;
            this.move(false);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void eat() {
        this.isUnderground = true;
        int foodEated = 0;
        try {
            foodEated = this.anthill._accesFood(50, this, false);
        } catch (final InterruptedException e1) {
            e1.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.anthill._foodAccessed();
        if (foodEated > 0) {
            this.nbrOfMealsTaken++;
            this.hunger = 0;
        } else {
            this.life = 0;
        }
    }

    private void patroll() {
        if (this.checkForPredator()) {
            this.choosePatrolTarget();
        }
        this.isUnderground = false;
        try {
            this.move(false);
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.searchFoodSpots();
        this.hunger++;
        while (this.hunger < 10) {
            if (this.checkForPredator()) {
                this.target.x = this.position.x;
                this.target.y = this.position.y;
            }
            int alea = this.rand.nextInt(2);
            int value = this.rand.nextInt(150) + 1;
            if (alea < 1) {
                this.target.x = this.target.x + value;
                if (this.target.x > Configuration.SQUARE_SIDE) {
                    this.target.x = Configuration.SQUARE_SIDE;
                }
            } else {
                this.target.x = this.target.x - value;
                if (this.target.x < 0) {
                    this.target.x = 0;
                }
            }
            alea = this.rand.nextInt(2);
            value = this.rand.nextInt(50) + 1;
            if (alea < 1) {
                this.target.y = this.target.y + value;
                if (this.target.y > Configuration.SQUARE_SIDE) {
                    this.target.y = Configuration.SQUARE_SIDE;
                }
            } else {
                this.target.y = this.target.y - value;
                if (this.target.y < 0) {
                    this.target.y = 0;
                }
            }
            try {
                this.move(false);
                Thread.sleep(this.rand.nextInt(2500) + 1L);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.searchFoodSpots();
            this.hunger++;
        }
    }

    private void searchFoodSpots() {
        for (final FoodSpot foodSpot : FoodSpots._getInstance()._getFoodSpots()) {
            if (!foodSpot._isDiscovered() && Collisions._getInstance()._isPointInObjectRange(foodSpot, this.position)) {
                foodSpot._setDiscovered(true);
            }
        }
    }
}
