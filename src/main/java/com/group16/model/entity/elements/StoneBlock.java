package com.group16.model.entity.elements;

import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.items.weapons.Sword;
import com.group16.model.utils.maths.Vector;

/**
 * Represents a defensive or structural stone block element placed by the player.
 * It can take damage but does not yield any resources when destroyed.
 */
public class StoneBlock extends Element {

    // The initial health/durability of the stone block
    private static final int STONE_BLOCK_HEALTH = 70;

    /**
     * Constructs a StoneBlock at the specified position with predefined health.
     *
     * @param position the map location where the block is placed
     */
    public StoneBlock(Vector position) {
        super(position, STONE_BLOCK_HEALTH);
    }

    /**
     * Stone blocks cannot be farmed, so this method is intentionally left empty.
     *
     * @param inventory the player's inventory (not used)
     */
    @Override
    public void farm(Inventory inventory) {}

    /**
     * Accepts interactions such as attacks from a sword. Reduces health accordingly.
     *
     * @param i                 the interactor (e.g., sword)
     * @param isCellInteraction true if interaction is from the tile directly (unused)
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        if (i instanceof Sword sword) {
            takeDamage(sword.getDamage());
        }
    }
}
