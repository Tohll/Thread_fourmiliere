package anthill.controllers;

import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.ImageIcon;

import anthill.ants.AbsCreep;
import anthill.interfaces.ObjectWithRange;
import anthill.monitors.FoodMonitor;
import anthill.threads.RunnableHolder;
import anthill.utils.Configuration;

/**
 * @author Seldan
 *
 */
public class Anthills implements ObjectWithRange {

    private static class SingletonHolder {
        private static final Anthills INSTANCE = new Anthills();
    }

    public static Anthills _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final FoodMonitor foodMonitor;
    private final int height;
    private final Point position;
    private RunnableHolder queen;
    private final Vector<Vector<Double>> range;
    private final ImageIcon sprite;
    private final int width;

    private Anthills() {
        this.width = Configuration.WIDTH / 10;
        this.height = Configuration.HEIGHT / 10;
        this.sprite = new ImageIcon("img/Ant_hill.png");
        this.position = new Point(Configuration.WIDTH / 2, Configuration.HEIGHT / 2);
        this.range = this._computeRange(this.position, this.width + 200, this.height + 200);
        this.foodMonitor = new FoodMonitor(Configuration.STARTING_FOOD_STOCK,
                Configuration.MAX_SOLDIERS_COUNT + Configuration.MAX_PEONS_COUNT);
    }

    /**
     * @param quantity
     * @param ant
     * @return the int result of this.foodMonitor._getFood(quantity, ant)
     * @throws InterruptedException
     */
    public int _accesFood(final int quantity, final AbsCreep ant, final boolean isDeposing)
            throws InterruptedException {
        return this.foodMonitor._accesFood(quantity, ant, isDeposing);
    }

    /**
     * @param foodFound
     * @throws InterruptedException
     */
    public void _foodAccessed() {
        this.foodMonitor._interactionDone();
    }

    public FoodMonitor _getFoodMonitor() {
        return this.foodMonitor;
    }

    public int _getHeight() {
        return this.height;
    }

    public Point _getPosition() {
        return this.position;
    }

    public RunnableHolder _getQueen() {
        return this.queen;
    }

    @Override
    public Vector<Vector<Double>> _getRange() {
        return this.range;
    }

    public int _getWidth() {
        return this.width;
    }

    public void _setQueen(final RunnableHolder queen) {
        this.queen = queen;
    }

    public Image getSprite() {
        return this.sprite.getImage();
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
