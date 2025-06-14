package com.group16.model.entity.elements;

import com.group16.model.entity.GameEntity;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.utils.maths.Vector;

/**
 * Represent an element in the game which has a position and is damageable.
 * This is an abstract class serving as the base for all static objects (like trees or rocks)
 * that players can interact with and gather resources from.
 */
public abstract class Element extends GameEntity implements Interactable {

    /**
     * Construct an element with a position and an initial health value.
     *
     * @param position the position of the element
     * @param health   the initial health value
     */
    public Element(Vector position, int health) {
        super(position, health);
    }

    /**
     * Applies damage to the element, reducing its health value.
     * Typically used when a player interacts with it using a tool.
     *
     * @param damage the amount of damage to apply to the element
     */
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    /**
     * Defines how the element provides resources to the inventory when farmed.
     *
     * @param inventory the inventory of the player or entity farming this element
     */
    public abstract void farm(Inventory inventory);

    /**
     * Defines how the element reacts to an interaction (e.g. being struck by a sword or tool).
     *
     * @param i                 the interactor performing the interaction
     * @param isCellInteraction whether the interaction is from a neighboring tile (true) or direct overlap (false)
     */
    @Override
    public abstract void acceptInteraction(Interactor i, boolean isCellInteraction);
}
