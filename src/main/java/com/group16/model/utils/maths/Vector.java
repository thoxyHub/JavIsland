package com.group16.model.utils.maths;

/**
 * A simple 2D vector class using float precision, supporting basic vector operations.
 */
public record Vector(float x, float y) {

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector add(float x, float y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector sub(float x, float y) {
        return new Vector(this.x - x, this.y - y);
    }

    public Vector scalarMul(float z) {
        return new Vector(z*this.x, z*this.y);
    }
}
