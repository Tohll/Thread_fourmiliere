package anthill.ants;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.utils.Configuration;

public class Predator extends AbsCreep {

    public Predator(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 10);
        this.isUnderground = true;
    }

    @Override
    protected void act() {
        this.defineStartingPosition();
        this.seek();
        this.destroy();
        if (this.anthill._getQueen()._getRunnable()._getLife() < 1) {
            this.life = 0;
        }
    }

    private void defineStartingPosition() {
        do {
            this.position.x = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
            this.position.y = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
        } while (Collisions._getInstance()._isPointInObjectRange(this.anthill, this.position) && this.anthill._getQueen()._getRunnable()._getLife() > 0);
    }

    private void destroy() {
        while (this.anthill._getQueen()._getRunnable()._getLife() > 0 && this.life > 0) {
            this.anthill._getQueen()._getRunnable()._receiveDamage(15);
            try {
                Thread.sleep(500L);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void seek() {
        this.isUnderground = false;
        this.target = this.anthill._getPosition();
        try {
            this.move(false);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
