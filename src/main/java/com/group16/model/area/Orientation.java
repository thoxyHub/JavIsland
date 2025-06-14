package com.group16.model.area;

import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;

/**
 * Enum representing the four cardinal orientations, with each mapped to
 * a directional vector scaled by tile size.
 */
public enum Orientation {

    NORTH(new Vector(0.0f, -GameConfig.TILE_SIZE)),

    EAST(new Vector(GameConfig.TILE_SIZE, 0.0f)),

    SOUTH(new Vector(0.0f, GameConfig.TILE_SIZE)),

    WEST(new Vector(-GameConfig.TILE_SIZE, 0.0f));

    private final Vector direction;

    /**
     * Constructs an orientation using the associated movement vector.
     *
     * @param direction vector representing movement direction
     */
    Orientation(Vector direction) {
        this.direction = direction;
    }

    /**
     * Returns the vector representation of this orientation.
     *
     * @return direction as a Vector
     */
    public Vector toVector() {
        return direction;
    }

    /**
     * Attempts to convert a vector into a corresponding cardinal orientation.
     * Returns null if the vector doesn't match a pure cardinal direction.
     *
     * @param v the input vector
     * @return Orientation corresponding to vector or null
     */
    public static Orientation fromVector(Vector v) {
        if (v.x() > 0 && v.y() == 0)
            return Orientation.EAST;
        if (v.x() < 0 && v.y() == 0)
            return Orientation.WEST;
        if (v.y() > 0 && v.x() == 0)
            return Orientation.NORTH;
        if (v.y() < 0 && v.x() == 0)
            return Orientation.SOUTH;
        return null;
    }
}
