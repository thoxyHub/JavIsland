package com.group16.model.area.tiles;

import com.group16.model.entity.GameEntity;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.entity.actors.Player;
import com.group16.model.entity.elements.*;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;

/**
 * Represents a single tile in the game map.
 * A tile can hold one entity at a time and may or may not be walkable depending on its type and content.
 */
public class Tile implements Interactable {

    /** Type of the tile (e.g. GRASS, WATER, etc.) */
    private final TileType type;

    /** Indicates whether the tile is walkable */
    private boolean isWalkable;

    /** Entity currently occupying the tile, if any */
    private GameEntity entity;

    /**
     * Constructs a tile of a specific type.
     * @param type the type of the tile
     */
    public Tile(TileType type) {
        this.type = type;
        this.isWalkable = type.isWalkable;
    }

    /**
     * Returns the entity currently on the tile.
     * @return the occupying GameEntity, or null if the tile is empty
     */
    public GameEntity getEntity() {
        return entity;
    }

    /**
     * Checks whether the tile is currently walkable.
     * @return true if the tile can be walked on
     */
    public boolean isWalkable() {
        return isWalkable;
    }

    /**
     * Returns the type of the tile.
     * @return the tile's type
     */
    public TileType getType() {
        return type;
    }

    /**
     * Adds an entity to the tile, marking it as not walkable if the entity takes up space.
     * @param entity the GameEntity to add
     */
    public void addEntity(GameEntity entity) {
        if (this.entity == null) {
            this.entity = entity;
            isWalkable = !entity.takeCellSpace();
        }
    }

    /**
     * Removes the current entity from the tile, restoring walkability based on tile type.
     */
    public void removeEntity() {
        if (entity != null) {
            entity = null;
            isWalkable = type.isWalkable;
        }
    }

    /**
     * Accepts interaction for building on the tile (e.g. placing a WoodBlock or StoneBlock).
     * Only the player can interact in this way.
     * @param i the interactor (must be Player)
     * @param isWood true if the player wants to build a WoodBlock, false for StoneBlock
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isWood) {
        assert(i instanceof Player) : "Only player can build on tiles";
        Player player = (Player) i;
        boolean isPlaceable = this.type != TileType.WATER && this.entity == null;
        if (!isPlaceable) {
            System.out.println("Can't build on this tile");
            return;
        }

        // Check if the player has enough resources to build and whether he wants to build a wood or stone block
        Element element = isWood ? new WoodBlock(player.getFieldOfViewCells()) :
                new StoneBlock(player.getFieldOfViewCells());
        if (isWood && player.getInventory().getAmount(new Resources(ResourceType.WOOD, 0)) <= 0) {
            System.out.println("You don't have enough wood to build");
            return;
        } else if (!isWood && player.getInventory().getAmount(new Resources(ResourceType.STONE, 0)) <= 0) {
            System.out.println("You don't have enough stone to build");
            return;
        }

        // Building the block
        player.build(this, element);
        if (isWood) player.getInventory().remove(new Resources(ResourceType.WOOD, 1));
        else player.getInventory().remove(new Resources(ResourceType.STONE, 1));
        this.isWalkable = false;
    }
}
