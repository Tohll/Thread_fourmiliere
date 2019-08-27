package anthill.ants;

import java.util.ArrayList;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.controllers.FoodSpots;
import anthill.food_spots.FoodSpot;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public class Peon extends AbsCreep {

    private int foodInInventory;
    private FoodSpot foodSpotCandidate;

    public Peon(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 2);
        this.name = "Peon " + number;
        this.foodSpotCandidate = null;
        this.foodInInventory = 0;
    }

    @Override
    protected void act() {
        if (this.life > 0) {
            this.seekSomethingToDo();
        }
        if (this.life > 0) {
            this.executeChoice();
        }
        if (this.life > 0) {
            this.gather();
        }
        if (this.life > 0) {
            this.comeBack();
        }
        if (this.life > 0) {
            this.deposit();
        }
    }

    private void comeBack() {
        this.isUnderground = false;
        this.target.x = Anthills._getInstance()._getPosition().x;
        this.target.y = Anthills._getInstance()._getPosition().y;
        try {
            this.move(true);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void deposit() {
        this.isUnderground = true;
        if (this.foodInInventory > 0) {
            try {
                this.anthill._accesFood(this.foodInInventory, this, true);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.anthill._foodAccessed();
        }
        this.foodInInventory = 0;
        this.eat();
    }

    private void eat() {
        this.isUnderground = true;
        int foodEated = 0;
        try {
            foodEated = this.anthill._accesFood(1, this, false);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.anthill._foodAccessed();
        if (foodEated > 0) {
            this.nbrOfMealsTaken++;
        } else {
            this.life = 0;
        }
    }

    private void executeChoice() {
        this.isUnderground = false;
        try {
            this.move(true);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void findASpotToSlack() {
        do {
            this.target.x = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
            this.target.y = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
        } while (!Collisions._getInstance()._isPointInObjectRange(this.anthill, this.target));
    }

    private void gather() {
        this.isUnderground = true;
        final int quantity = 3;
        try {
            this.foodInInventory = this.foodSpotCandidate._getFood(quantity, this);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        if (this.foodSpotCandidate._getFoodStock() < quantity) {
            this.foodSpotCandidate._setEmpty(true);
        }
        try {
            Thread.sleep(10);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.foodSpotCandidate._foodTaken();
        this.foodSpotCandidate = null;
    }

    private void seekSomethingToDo() {
        ArrayList<FoodSpot> candidate = null;
        do {
            this.isUnderground = false;
            this.findASpotToSlack();
            try {
                this.move(true);
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
                Thread.currentThread().interrupt();
            }
            final int timer = this.rand.nextInt(5950) + 51;
            try {
                Thread.sleep(timer);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.comeBack();
            candidate = (ArrayList<FoodSpot>) FoodSpots._getInstance()._findARandomDiscoveredFoodSpot();
            if (candidate.isEmpty()) {
                this.eat();
            }
        } while (candidate.isEmpty() && this.life > 0);
        if (!candidate.isEmpty()) {
            this.foodSpotCandidate = candidate.get(0);
            this.target.x = candidate.get(0)._getPosition().x;
            this.target.y = candidate.get(0)._getPosition().y;
        }
    }
}
