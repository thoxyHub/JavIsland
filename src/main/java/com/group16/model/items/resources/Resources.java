package com.group16.model.items.resources;

/**
 * Represents a specific type of resource in the game, such as wood, stone, or ammo.
 * Inherits quantity behavior from the abstract Item class.
 */
public class Resources extends Item {

    // The type of the resource (e.g., WOOD, STONE, AMMO)
    private final ResourceType type;

    /**
     * Constructs a new resource with the specified type and quantity.
     *
     * @param type     the type of resource
     * @param quantity the amount of the resource
     */
    public Resources(ResourceType type, int quantity) {
        super(quantity);
        this.type = type;
    }

    /**
     * Returns the type of the resource.
     *
     * @return the resource type
     */
    public ResourceType getType() {
        return type;
    }
}
