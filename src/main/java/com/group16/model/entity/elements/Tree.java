package com.group16.model.entity.elements;

import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;
import com.group16.model.items.weapons.Sword;
import com.group16.model.utils.maths.Vector;

/**
 * Represents a tree element on the map that can be farmed for wood.
 * Trees have health and disappear when their health reaches zero.
 */
public class Tree extends Element {

    // The initial health of the tree
    private static final int TREE_HEALTH = 100;

    // The amount of wood gained from farming a tree
    private static final int FARMING_AMOUNT = 2;

    /**
     * Constructs a tree element at a specified position with a fixed health value.
     *
     * @param position the position of the tree on the island
     */
    public Tree(Vector position) {
        super(position, TREE_HEALTH);
    }

    /**
     * Adds a predefined amount of wood to the player's inventory when the tree is farmed.
     *
     * @param inventory the player's inventory to receive the resources
     */
    @Override
    public void farm(Inventory inventory) {
        inventory.add(new Resources(ResourceType.WOOD, FARMING_AMOUNT));
    }

    /**
     * Allows interaction with the tree. Takes damage.
     *
     * @param i                 the interactor (e.g., Sword)
     * @param isCellInteraction whether the interaction is tile-based (unused)
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        if (i instanceof Sword sword) {
            takeDamage(sword.getDamage());
        }
    }
}
