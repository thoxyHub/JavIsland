package com.group16.model.items;

/**
 * Represents an item that can be used by the player, such as weapons or consumables.
 */
public interface Usable {
    /**
     * Executes the use behavior of the item.
     *
     * @param inventory the inventory context in which the item is used
     */
    void use(Inventory inventory);
}
