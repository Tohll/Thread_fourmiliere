package anthill.controllers;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import anthill.food_spots.FoodSpot;
import anthill.utils.Configuration;

public class FoodSpots {

    private static class SingletonHolder {
        private static final FoodSpots INSTANCE = new FoodSpots();
    }

    public static FoodSpots _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final FoodSpot[] foodSpotsArray;
    private final Random rand;
    private final ImageIcon sprite;

    private FoodSpots() {
        this.sprite = new ImageIcon("img/food_spot.png");
        this.rand = new Random();
        this.foodSpotsArray = new FoodSpot[Configuration.FOOD_SPOTS];
        this.initFoodSpots();

    }

    public List<FoodSpot> _findARandomDiscoveredFoodSpot() {
        final ArrayList<FoodSpot> list = new ArrayList<>();
        final ArrayList<FoodSpot> discovered = new ArrayList<>();
        for (final FoodSpot foodSpot : this.foodSpotsArray) {
            if (foodSpot._isDiscovered() && !foodSpot._isEmpty()) {
                discovered.add(foodSpot);
            }
        }
        if (!discovered.isEmpty()) {
            list.add(discovered.get(this.rand.nextInt(discovered.size())));
        }
        return list;
    }

    public FoodSpot[] _getFoodSpots() {
        return this.foodSpotsArray;
    }

    public Image _getSprite() {
        return this.sprite.getImage();
    }

    private void initFoodSpots() {
        for (int i = 0; i < this.foodSpotsArray.length; i++) {
            this.foodSpotsArray[i] = new FoodSpot(this.rand.nextInt(1001) + 1000,
                    new Point(this.rand.nextInt(Configuration.SQUARE_SIDE) + 1,
                            this.rand.nextInt(Configuration.SQUARE_SIDE) + 1));
        }
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }

}
