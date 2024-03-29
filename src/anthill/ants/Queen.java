package anthill.ants;

import anthill.controllers.Anthills;
import anthill.controllers.Creeps;
import anthill.threads.RunnableHolder;
import anthill.utils.Configuration;

public class Queen extends AbsCreep {

    private int indexPeons;
    private int indexSoldiers;

    public Queen(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 1);
        this.indexPeons = 1;
        this.indexSoldiers = 1;
        this.position = anthill._getPosition();
    }

    @Override
    protected void act() {
        if (this.life > 0) {
            this.hatchEggs();
        }
        if (this.life > 0) {
            this.eat();
        }
    }

    private void eat() {
        int foodTaken = 0;
        try {
            foodTaken = this.anthill._accesFood(200, this, false);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        this.anthill._foodAccessed();
        if (foodTaken < 1) {
            this.life = this.life - 25;
        } else {
            if (this.life < this.maxLife) {
                this.life = this.life + 5;
            }
        }
    }

    private void hatchEggs() {
        this.hatchSoldiers(3);
        this.hatchPeons(100);
        try {
            Thread.sleep(this.rand.nextInt(5001) + 5000L);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void hatchPeons(final int aleaOfPeons) {
        int nbrOfPeons = this.rand.nextInt(aleaOfPeons) + 1;
        if (nbrOfPeons + this.indexPeons > Configuration.MAX_PEONS_COUNT) {
            nbrOfPeons = 0;
        }
        for (int i = 1; i <= nbrOfPeons; i++) {
            final RunnableHolder peon = new RunnableHolder(
                    new Peon(i, Configuration.SOLDIERS_LIFE, Anthills._getInstance()),
                    String.format("Peon [%d] %d", this.number, this.indexPeons));
            Creeps._getInstance()._getPeons().add(peon);
            peon.start();
            this.indexPeons++;
        }
    }

    private void hatchSoldiers(final int aleaOfSoldiers) {
        int nbrOfSoldiers = this.rand.nextInt(aleaOfSoldiers);
        if (this.indexSoldiers == 1) {
            nbrOfSoldiers = 3;
        }
        if (nbrOfSoldiers + this.indexSoldiers > Configuration.MAX_SOLDIERS_COUNT) {
            nbrOfSoldiers = 0;
        }
        for (int i = 1; i <= nbrOfSoldiers; i++) {
            final RunnableHolder soldier = new RunnableHolder(
                    new Soldier(i, Configuration.SOLDIERS_LIFE, Anthills._getInstance()),
                    String.format("Soldier [%d] %d", this.number, this.indexSoldiers));
            Creeps._getInstance()._getSoldiers().add(soldier);
            soldier.start();
            this.indexSoldiers++;
        }
    }
}
