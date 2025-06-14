package com.group16.model.area.tiles;

/**
 * Enum representing the different types of tiles in the game.
 * Each tile type defines whether it is walkable or not.
 */
public enum TileType {

    WATER(false),

    GRASS(true),

    SAND(true),

    HILL(false);

    /** Whether the tile type can be walked on */
    public final boolean isWalkable;

    /**
     * Constructor for each tile type, defining walkability.
     * @param isWalkable whether this tile type allows entities to walk on it
     */
    TileType(boolean isWalkable) {
        this.isWalkable = isWalkable;
    }
}
