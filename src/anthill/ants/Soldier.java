package anthill.ants;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.controllers.FoodSpots;
import anthill.food_spots.FoodSpot;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public final class Soldier extends AbsAnt {

    public Soldier(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 5);
        this.name = "Soldier " + number;
    }

    @Override
    protected void act() {
        this.choosePatrolTarget();
        this.patroll();
        this.comeback();
        this.eat();
    }

    private void choosePatrolTarget() {
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
            foodEated = this.anthill._accesFood(100, this, false);
        } catch (final InterruptedException e1) {
            e1.printStackTrace();
            Thread.currentThread().interrupt();
        }
        if (foodEated > 0) {
            this.anthill._foodAccessed();
            this.nbrOfMealsTaken++;
            this.hunger = 0;
        } else {
            this.anthill._foodAccessed();
            this.life = 0;
        }
    }

    private void patroll() {
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
            if (Collisions._getInstance()._isPointInObjectRange(foodSpot, this.position) && !foodSpot._isDiscovered()) {
                foodSpot._setDiscovered(true);
            }
        }
    }
}
