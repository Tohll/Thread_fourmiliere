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
public final class Soldier extends AbsCreep {

    private boolean isAttacking;
    private boolean isSearching;
    private RunnableHolder targetedPredator;

    public Soldier(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 3);
        this.isAttacking = false;
        this.isSearching = false;
        this.name = "Soldier " + number;
        this.targetedPredator = null;
    }

    public boolean _isAttacking() {
        return this.isAttacking;
    }

    public boolean _isSearching() {
        return this.isSearching;
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

    private void attack() {
        this.isAttacking = true;
        this.target = this.targetedPredator._getRunnable()._getPosition();
        try {
            this.move(true);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.isAttacking = false;
        while (this.targetedPredator._getRunnable()._getLife() > 0) {
            this.target = this.targetedPredator._getRunnable()._getPosition();
            try {
                this.move(true);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            if (this.targetedPredator._getRunnable()._getLife() > 0) {
                this.targetedPredator._getRunnable()._receiveDamage(1);
            }
            this.target = new Point();
            this.target.x = this.position.x;
            this.target.y = this.position.y;
            this.moveToCloseRandomPoint(2);
        }
        this.targetedPredator = null;
        this.target = new Point();
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
            this.isUnderground = false;
            if (activesPredators.size() == 1) {
                this.targetedPredator = activesPredators.get(0);
            } else {
                this.targetedPredator = activesPredators.get(this.rand.nextInt(activesPredators.size()));
            }
            this.attack();
        }
        return predatorFound;
    }

    private void choosePatrolTarget() {
        this.checkForPredator();
        this.target.x = this.rand.nextInt(Configuration.WIDTH) + 1;
        this.target.y = this.rand.nextInt(Configuration.HEIGHT) + 1;
    }

    private void comeback() {
        try {
            this.target.x = this.anthill._getPosition().x;
            this.target.y = this.anthill._getPosition().y;
            this.move(true);
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
        } catch (final InterruptedException e) {
            e.printStackTrace();
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
            this.move(true);
            this.isSearching = true;
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.isSearching = false;
        this.searchFoodSpots();
        this.hunger++;
        while (this.hunger < 10) {
            if (this.checkForPredator()) {
                this.target.x = this.position.x;
                this.target.y = this.position.y;
            }
            this.moveToCloseRandomPoint(150);
            this.isSearching = true;
            try {
                Thread.sleep(this.rand.nextInt(2500) + 1L);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.isSearching = false;
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
