package anthill1_0.utils;

import java.awt.Point;
import java.io.Serializable;

public class SpriteInfos implements Serializable {
    private static final long serialVersionUID = 437336186909180626L;
    private final Point position;
    private final float rotate;
    private final int width;

    public SpriteInfos(final float rotate, final int width, final Point position) {
        this.rotate = rotate;
        this.width = width;
        this.position = position;
    }

    public Point _getPosition() {
        return this.position;
    }

    public float _getRotate() {
        return this.rotate;
    }

    public int _getWidth() {
        return this.width;
    }
}
