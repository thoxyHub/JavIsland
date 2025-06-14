package com.group16.model.entity;

/**
 * Represents entities that can initiate interactions with Interactable objects.
 * Examples include the player, mobs, projectiles, or tools like a sword.
 */
public interface Interactor {

    /**
     * Performs an interaction with a target Interactable.
     *
     * @param other the target object being interacted with
     * @param isCellInteraction true if the interaction happens from the same tile (e.g., melee attack),
     *                          false if it's a ranged interaction (e.g., shooting)
     */
    void interactWith(Interactable other, boolean isCellInteraction);
}
