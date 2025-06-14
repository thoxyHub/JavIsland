package com.group16.model.entity.elements;

import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;
import com.group16.model.items.weapons.Sword;
import com.group16.model.utils.maths.Vector;

/**
 * A Rock is a stationary element in the game that can be farmed for stone resources.
 * It has a fixed amount of health and interacts for farming.
 */
public class Rock extends Element {

    // The initial health of the rock
    private static final int ROCK_HEALTH = 150;

    // The amount of stone given to the inventory when farmed
    private static final int FARMING_AMOUNT = 1;

    /**
     * Creates a new Rock entity at the given position with default health.
     *
     * @param position the position where the rock is placed on the map
     */
    public Rock(Vector position) {
        super(position, ROCK_HEALTH);
    }

    /**
     * Adds stone resources to the provided inventory when the rock is farmed.
     *
     * @param inventory the player's inventory
     */
    @Override
    public void farm(Inventory inventory) {
        inventory.add(new Resources(ResourceType.STONE, FARMING_AMOUNT));
    }

    /**
     * Handles interaction logic when the rock is hit.
     * The rock will take damage based on the hitter damage value.
     *
     * @param i                 the interactor (should be a sword)
     * @param isCellInteraction not used here
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        if (i instanceof Sword sword) {
            takeDamage(sword.getDamage());
        }
    }
}
