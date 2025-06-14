package com.group16.model.entity;

/**
 * Interface for any entity that can be interacted with by an Interactor.
 * Typically implemented by things like tiles, resources, and other game entities.
 */
public interface Interactable {
    /**
     * Defines how this object reacts when another entity interacts with it.
     *
     * @param i the interactor attempting to interact
     * @param isCellInteraction true if the interaction occurs from the same tile;
     *                          false if from a distance (e.g., ranged attack)
     */
    void acceptInteraction(Interactor i, boolean isCellInteraction);
}
