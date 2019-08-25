package anthill.controllers;

import java.awt.Point;

import anthill.interfaces.ObjectWithRange;

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

    public boolean isClose(final Point position, final Point target, final int fuzzIndex) {
        boolean isClose = false;
        final int fuzz = fuzzIndex < 0 ? 0 : fuzzIndex;
        if (Math.abs(position.x - target.x) < fuzz + 2 && Math.abs(position.y - target.y) < fuzz + 2) {
            isClose = true;
        }
        return isClose;
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
