package anthill.ants;

import anthill.controllers.Anthills;
import anthill.controllers.Collisions;
import anthill.utils.Configuration;

public class Predator extends AbsAnt {

    public Predator(final int number, final int life, final Anthills anthill) {
        super(number, life, anthill, 10);
        this.isUnderground = true;
    }

    @Override
    protected void act() {
        this.defineStartingPosition();
        this.seek();
        this.destroy();
    }

    private void defineStartingPosition() {
        do {
            this.position.x = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
            this.position.y = this.rand.nextInt(Configuration.SQUARE_SIDE) + 1;
        } while (Collisions._getInstance()._isPointInObjectRange(this.anthill, this.position));
    }

    private void destroy() {
        this.life = 0;
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
