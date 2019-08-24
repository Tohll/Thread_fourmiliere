package anthill1_0.interfaces;

import java.awt.Point;
import java.util.Vector;

public interface ObjectWithRange {
    /**
     * Generate a range adapted for a given object.
     *
     * @param position Your object's position.
     * @param width    Your object's width.
     * @param height   your object's height.
     * @return a two dimensions array of coordinates representing the range, where
     *         indexes are like :</br>
     *         |00(x1)|01(y1)|</br>
     *         |10(x2)|11(y2)|
     */
    public default Vector<Vector<Double>> _computeRange(final Point position, final int width, final int height) {
        final Vector<Vector<Double>> matrix = new Vector<>();
        matrix.add(new Vector<Double>());
        matrix.add(new Vector<Double>());
        matrix.get(0).add((position.x - (width / 2d)) - 10d);
        matrix.get(0).add((position.y - (height / 2d)) - 10d);
        matrix.get(1).add((position.x + (width / 2d)) + 10d);
        matrix.get(1).add((position.y + (height / 2d)) + 10d);
        return matrix;
    }

    /**
     * You should use the build in _computeRange(final Point position, final int
     * width, final int height) to initialize a field or like this :</br>
     * </br>
     * public Vector<Vector<Double>> _getRange() {</br>
     * return this._computeRange(this.position, this.width, this.height);</br>
     * }</br>
     *
     * @return a two dimensions array of coordinates where indexes are like :</br>
     *         |00(x1)|01(y1)|</br>
     *         |10(x2)|11(y2)|
     */
    public Vector<Vector<Double>> _getRange();
}