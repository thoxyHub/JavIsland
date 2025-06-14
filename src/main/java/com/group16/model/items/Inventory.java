package com.group16.model.items;

import com.group16.model.items.resources.Item;
import com.group16.model.Subject;
import com.group16.view.Observer;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's inventory that can store and manage items.
 * Supports observers for UI updates and includes a pocket system for quick access to usable items.
 */
public class Inventory implements Subject {

    private final List<Item> inventory;
    private boolean isOpen;

    private final List<Observer> observers = new ArrayList<>();

    public static final int NB_OF_POCKETS = 2;
    private final Usable[] pockets = new Usable[NB_OF_POCKETS];

    /**
     * Constructs an empty inventory.
     */
    public Inventory() {
        inventory = new ArrayList<>();
        isOpen = false;
    }

    /**
     * Gets the quantity of a specific item in the inventory.
     * Assumes the item is already present.
     *
     * @param item the item to check
     * @return quantity of the item, or 0 if not found
     */
    public int getAmount(Item item) {
        assert (inventory.contains(item));
        int index = inventory.indexOf(item);
        return index < 0 ? 0 : inventory.get(index).getQuantity();
    }

    /**
     * Adds an item to the inventory. If it already exists, the quantity is increased.
     *
     * @param item the item to add
     */
    public void add(Item item) {
        int index = inventory.indexOf(item);

        if (index < 0) {
            inventory.add(item);
        } else {
            Item currentItem = inventory.get(index);
            int actualQuantity = currentItem.getQuantity();
            currentItem.setQuantity(actualQuantity + item.getQuantity());
        }
        notifyObserver();
    }

    /**
     * Removes the specified quantity of an item from the inventory.
     * If quantity drops to zero or below, the item is removed completely.
     *
     * @param item the item to remove
     */
    public void remove(Item item) {
        assert inventory.contains(item);

        Item currentItem = inventory.get(inventory.indexOf(item));
        int newQuantity = currentItem.getQuantity() - item.getQuantity();

        if (newQuantity <= 0) {
            inventory.remove(item);
        } else {
            currentItem.setQuantity(newQuantity);
        }

        notifyObserver();
    }

    /**
     * Gets an unmodifiable view of the current inventory items.
     *
     * @return list of items
     */
    public List<Item> getContents() {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * Returns whether the inventory is open (visible).
     *
     * @return true if open, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Toggles the inventory open/closed state.
     */
    public void changeOpenClose() {
        isOpen = !isOpen;
        notifyObserver();
    }

    /**
     * Assigns an item to a pocket slot for quick use, if the item is in the inventory and usable.
     *
     * @param index the pocket index (0 or 1)
     * @param item  the item to assign
     */
    public void setPocketItem(int index, Item item) {
        if (index < 0 || index >= pockets.length) {
            throw new IllegalArgumentException("Pocket index out of bounds");
        }
        if (inventory.contains(item) && item instanceof Usable) {
            pockets[index] = (Usable) item;
            notifyObserver();
        } else {
            System.out.println("Cannot assign an item that's not in the inventory.");
        }
        notifyObserver();
    }

    /**
     * Retrieves the item currently assigned to the given pocket.
     *
     * @param index the pocket index
     * @return the usable item, or null if the index is invalid
     */
    public Usable getPocketItem(int index) {
        if (index < 0 || index >= NB_OF_POCKETS) {
            return null;
        }
        return pockets[index];
    }

    @Override
    public void registerObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
            notifyObserver();
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
