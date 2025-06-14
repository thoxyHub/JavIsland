package com.group16.model.entity;

import com.group16.model.utils.maths.Vector;
import com.group16.model.Subject;
import com.group16.view.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic game entity with a position and health.
 * Acts as a base class for all interactive or visible game elements.
 * Implements the Subject interface for observer notifications.
 */
public abstract class GameEntity implements Subject {

    // The entity's position on the game map
    private Vector position;

    // The health of the entity (e.g., used for damage, destruction)
    protected int health;

    // List of observers that will be notified on state changes
    private List<Observer> observers = new ArrayList<>();

    /**
     * Constructs a game entity with a given position and health.
     *
     * @param position initial position of the entity
     * @param health   starting health of the entity
     */
    public GameEntity(Vector position, int health) {
        this.position = position;
        this.health = health;
    }

    /**
     * Gets the current position of the entity.
     *
     * @return the position vector
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * Updates the entity's position and notifies observers.
     *
     * @param position the new position vector
     */
    protected void setPosition(Vector position) {
        this.position = position;
        notifyObserver();
    }

    /**
     * Returns the current health value.
     *
     * @return the health as an int
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Updates the health value and notifies observers.
     *
     * @param health new health value
     */
    protected void setHealth(int health) {
        this.health = health;
        notifyObserver();
    }

    /**
     * Indicates whether the entity blocks movement on its tile.
     * Override to allow entities like projectiles to pass through.
     *
     * @return true if the entity occupies space, false otherwise
     */
    public boolean takeCellSpace() {
        return true;
    }

    /*//////////////////////////////////////////////////////////////
                         Observer Pattern Methods
    //////////////////////////////////////////////////////////////*/

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(o -> o.update(this));
    }
}
