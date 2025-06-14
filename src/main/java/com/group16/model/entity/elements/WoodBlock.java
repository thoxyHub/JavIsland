package com.group16.model.entity.elements;

import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.items.weapons.Sword;
import com.group16.model.utils.maths.Vector;

/**
 * Represents a buildable wooden block in the game.
 * Acts as a barrier or structure placed by the player.
 */
public class WoodBlock extends Element {

    // The initial health value of a wooden block
    private static final int WOOD_BLOCK_HEALTH = 50;

    /**
     * Constructs a wooden block at a specified position with fixed health.
     *
     * @param position the location where the block is placed
     */
    public WoodBlock(Vector position) {
        super(position, WOOD_BLOCK_HEALTH);
    }

    /**
     * Wood Blocks don't provide resources when farmed.
     *
     * @param inventory the player's inventory (unused)
     */
    @Override
    public void farm(Inventory inventory) {
        // Intentionally left empty as WoodBlocks are not farmable
    }

    /**
     * Allows the block to be damaged by a sword interaction.
     *
     * @param i                 the interactor (should be a Sword)
     * @param isCellInteraction whether the interaction is tile-based (unused)
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        if (i instanceof Sword sword) {
            takeDamage(sword.getDamage());
        }
    }
}
