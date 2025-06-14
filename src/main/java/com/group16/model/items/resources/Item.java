package com.group16.model.items.resources;

import com.group16.model.Subject;
import com.group16.view.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing an item in the game that can be stored in an inventory.
 * All items have a quantity and can be observed for changes.
 */
public abstract class Item implements Subject {

    // List of observers (typically UI components) watching this item
    private List<Observer> observers = new ArrayList<>();

    // Quantity of the item (e.g., 5 wood or 10 ammo)
    private int quantity;

    /**
     * Constructs an item with a given initial quantity.
     *
     * @param quantity initial quantity of the item
     */
    public Item(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the quantity of the item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets a new quantity and notifies observers.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyObserver();
    }

    /* ==== Observer pattern methods ==== */

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

    /* ==== Equality and Hashing ==== */

    /**
     * Two items are considered equal if:
     * - They are both Resources and have the same ResourceType.
     * - Or they are of the exact same class.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item other)) return false;

        // If both are resources, compare by ResourceType
        if (this instanceof Resources res1 && other instanceof Resources res2) {
            return res1.getType() == res2.getType();
        }

        // Otherwise, fallback to same class comparison
        return this.getClass().equals(other.getClass());
    }

    @Override
    public int hashCode() {
        if (this instanceof Resources res) {
            return res.getType().hashCode(); // enum hashCode is stable
        } else {
            return this.getClass().hashCode();
        }
    }
}
