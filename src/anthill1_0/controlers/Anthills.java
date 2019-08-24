package anthill1_0.controlers;

import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.ImageIcon;

import anthill1_0.ants.AbsAnt;
import anthill1_0.ants.Queen;
import anthill1_0.interfaces.ObjectWithRange;
import anthill1_0.monitors.FoodMonitor;
import anthill1_0.threads.RunnableHolder;
import anthill1_0.utils.Configuration;

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
    private final Vector<Vector<Double>> range;
    private final ImageIcon sprite;
    private final int width;

    private Anthills() {
        this.width = Configuration.SQUARE_SIDE / 10;
        this.height = Configuration.SQUARE_SIDE / 10;
        this.sprite = new ImageIcon("img/Ant_hill.png");
        this.position = new Point(Configuration.SQUARE_SIDE / 2, Configuration.SQUARE_SIDE / 2);
        this.range = this._computeRange(this.position, this.width + 200, this.height + 200);
        this.foodMonitor = new FoodMonitor(Configuration.STARTING_FOOD_STOCK, Configuration.MAX_SOLDIERS_COUNT);
    }

    public void _depositFood(final int quantity) throws InterruptedException {
        this.foodMonitor._addFood(quantity);
    }

    /**
     * @param foodFound
     * @throws InterruptedException
     */
    public void _foodTaken() {
        this.foodMonitor._interactionDone();
    }

    /**
     * @param quantity
     * @param ant
     * @return the int result of this.foodMonitor._getFood(quantity, ant)
     * @throws InterruptedException
     */
    public int _getFood(final int quantity, final AbsAnt ant) throws InterruptedException {
        return this.foodMonitor._getFood(quantity, ant);
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

    @Override
    public Vector<Vector<Double>> _getRange() {
        return this.range;
    }

    public int _getWidth() {
        return this.width;
    }

    public Image getSprite() {
        return this.sprite.getImage();
    }

    public void initQueens() {
        final RunnableHolder queen = new RunnableHolder(new Queen(1, 50, Anthills._getInstance()), "Queen 1");
        queen.start();
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
