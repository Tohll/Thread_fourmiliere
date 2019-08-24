package anthill.food_spots;

import java.awt.Point;
import java.util.Vector;

import anthill.ants.AbsAnt;
import anthill.interfaces.ObjectWithRange;
import anthill.monitors.FoodMonitor;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public class FoodSpot implements ObjectWithRange {

    private final FoodMonitor foodMonitor;
    private final int height;
    private boolean isDiscovered;
    private boolean isEmpty;
    private final Point position;
    private final Vector<Vector<Double>> range;
    private final int width;

    public FoodSpot(final int foodStock, final Point position) {
        this.isEmpty = false;
        this.isDiscovered = false;
        this.width = 80;
        this.height = 35;
        this.position = position;
        this.range = this._computeRange(this.position, this.width, this.height);
        this.foodMonitor = new FoodMonitor(foodStock, Configuration.MAX_PEONS_COUNT);
    }

    public void _foodTaken() {
        this.foodMonitor._interactionDone();
    }

    public int _getFood(final int quantity, final AbsAnt ant) throws InterruptedException {
        return this.foodMonitor._accesFood(quantity, ant, false);
    }

    public int _getFoodStock() {
        return this.foodMonitor._getFoodStock();
    }

    public int _getHeight() {
        return this.height;
    }

    public Point _getPosition() {
        return this.position;
    }

    @Override
    public Vector<Vector<Double>> _getRange() {
        return this.range;
    }

    public int _getWidth() {
        return this.width;
    }

    public boolean _isDiscovered() {
        return this.isDiscovered;
    }

    public boolean _isEmpty() {
        return this.isEmpty;
    }

    public void _setDiscovered(final boolean isDiscovered) {
        this.isDiscovered = isDiscovered;
    }

    public void _setEmpty(final boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}