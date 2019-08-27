package anthill.utils;

public class Configuration {

    /**
     * Number of food spots generated and hidden
     */
    public static final int FOOD_SPOTS = 15;
    /**
     * Application height</br>
     * Must be >= 500 && <= your resolution height
     */
    public static final int HEIGHT = 600;
    /**
     * Maximum peons that can be generated on the map
     */
    public static final int MAX_PEONS_COUNT = 2500;
    /**
     * Maximum soldiers that can be generated on the map
     */
    public static final int MAX_SOLDIERS_COUNT = 30;
    /**
     * Maximum predator life
     */
    public static final int PREDATOR_LIFE = 70;
    /**
     * Maximum queen life
     */
    public static final int QUEEN_LIFE = 500;
    /**
     * How many grass to be draw
     */
    public static final int SCENERY_DENSITY = 5000;
    /**
     * Maximum soldier life
     */
    public static final int SOLDIERS_LIFE = 50;
    /**
     * How much food the anthill start with
     */
    public static final int STARTING_FOOD_STOCK = 4000;
    /**
     * Application width</br>
     * Must be >= 500 && <= your resolution width
     */
    public static final int WIDTH = 800;

    private Configuration() {

    }
}
