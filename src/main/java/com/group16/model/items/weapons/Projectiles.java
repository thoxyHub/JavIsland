package com.group16.model.items.weapons;

import com.group16.model.entity.GameEntity;
import com.group16.model.area.Island;
import com.group16.model.area.Orientation;
import com.group16.controller.Updatable;
import com.group16.model.utils.maths.Vector;

/**
 * Abstract class representing a projectile in the game, such as a bullet.
 * Projectiles move in a specific direction and have a fixed damage value.
 */
public abstract class Projectiles extends GameEntity implements Updatable {

    // Direction the projectile is moving in
    private final Orientation orientation;

    // Time remaining before the next allowed move
    private float lastMoveTime;

    // Time interval between moves (in nanoseconds)
    private final float MOVE_INTERVAL;

    // Amount of damage this projectile deals
    private final int damage;

    /**
     * Constructs a projectile.
     *
     * @param damage        the damage dealt by the projectile
     * @param start         the starting position of the projectile
     * @param direction     the direction in which the projectile moves
     * @param moveInterval  how often the projectile moves (in nanoseconds)
     */
    public Projectiles(int damage, Vector start, Orientation direction, float moveInterval) {
        super(start, 1); // Projectiles usually have 1 HP (disappear on impact)
        this.orientation = direction;
        this.MOVE_INTERVAL = moveInterval;
        this.damage = damage;
    }

    /**
     * Updates the projectile's state. Subclasses must define how the projectile behaves.
     *
     * @param deltaTime time elapsed since last update (in nanoseconds)
     */
    public abstract void update(float deltaTime);

    /**
     * Removes the projectile from the island (both logically and visually).
     *
     * @param x      the x-coordinate of the projectile
     * @param y      the y-coordinate of the projectile
     * @param island the island containing the projectile
     */
    protected void removeProjectile(int x, int y, Island island) {
        island.getTile(x, y).removeEntity();
        island.removeEntity(this);
        setLastMoveTime(getMoveInterval());
    }

    /* ==================== Getters ==================== */

    /**
     * @return the direction of the projectile
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * @return the time remaining until the next movement
     */
    public float getLastMoveTime() {
        return this.lastMoveTime;
    }

    /**
     * @return the fixed interval between movements
     */
    public float getMoveInterval() {
        return this.MOVE_INTERVAL;
    }

    /**
     * @return the damage this projectile deals on impact
     */
    public int getDamage() {
        return damage;
    }

    /* ==================== Setters ==================== */

    /**
     * Sets the time remaining until the next move.
     *
     * @param lastMoveTime time in nanoseconds
     */
    protected void setLastMoveTime(float lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
        notifyObserver();
    }
}
