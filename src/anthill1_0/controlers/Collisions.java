package anthill1_0.controlers;

import java.awt.Point;

import anthill1_0.interfaces.ObjectWithRange;

public class Collisions {

    private static class SingletonHolder {
        private static final Collisions INSTANCE = new Collisions();
    }

    public static Collisions _getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Collisions() {

    }

    public boolean _isPointInObjectRange(final ObjectWithRange objectWithRange, final Point position) {
        return position.x >= objectWithRange._getRange().get(0).get(0)
                && position.y >= objectWithRange._getRange().get(0).get(1)
                && position.x <= objectWithRange._getRange().get(1).get(0)
                && position.y <= objectWithRange._getRange().get(1).get(1);
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
