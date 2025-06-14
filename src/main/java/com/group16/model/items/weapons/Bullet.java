package com.group16.model.items.weapons;

import com.group16.model.entity.GameEntity;
import com.group16.model.entity.actors.Actors;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.area.Orientation;
import com.group16.model.area.tiles.Tile;
import com.group16.model.area.tiles.TileType;
import com.group16.model.entity.elements.Element;
import com.group16.model.area.Island;
import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;

/**
 * Represents a bullet projectile that moves in a given direction
 * and interacts with other entities on collision.
 */
public class Bullet extends Projectiles implements Interactor {

    // Time between moves in nanoseconds
    private static final float MOVE_INTERVAL = 1e8F;

    private Island island;

    /**
     * Constructs a bullet with specified damage, starting position, and direction.
     *
     * @param damage    Damage dealt by the bullet.
     * @param start     Starting position of the bullet.
     * @param direction Direction the bullet moves in.
     */
    public Bullet(int damage, Vector start, Orientation direction) {
        super(damage, start, direction, MOVE_INTERVAL);
        setLastMoveTime(getMoveInterval());
    }

    /**
     * Sets the island where the bullet is moving.
     *
     * @param island The island instance.
     */
    public void setIsland(Island island) {
        this.island = island;
    }

    /**
     * Updates bullet's movement and handles interaction with entities.
     *
     * @param deltaTime Time passed since last update in nanoseconds.
     */
    @Override
    public void update(float deltaTime) {
        // If the bullet is inactive or not assigned an island, skip update
        if (getHealth() <= 0 || island == null) return;

        setLastMoveTime(getLastMoveTime() - deltaTime);

        if (getLastMoveTime() < 0) {
            int currX = (int) getPosition().x();
            int currY = (int) getPosition().y();
            int targetX = (int) (currX + getOrientation().toVector().x() / GameConfig.TILE_SIZE);
            int targetY = (int) (currY + getOrientation().toVector().y() / GameConfig.TILE_SIZE);

            // Check bounds
            if (targetY >= 0 && targetY < island.getMapHeight() && targetX >= 0 && targetX < island.getMapWidth()) {
                Tile targetTile = island.getTile(targetX, targetY);
                GameEntity entity = targetTile.getEntity();

                // Collides with actor (mob or player)
                if (entity instanceof Actors actor) {
                    this.interactWith(actor, true);
                    removeProjectile(currX, currY, island);
                    setHealth(0);
                    return;
                }

                // Collides with obstacle like element (e.g., tree or rock)
                if (entity instanceof Element) {
                    setHealth(0);
                    return;
                }
            } else {
                // Out of bounds, destroy the bullet
                setHealth(0);
                return;
            }

            // Move forward on the map
            island.getTile(currX, currY).removeEntity();
            setPosition(new Vector(targetX, targetY));
            island.getTile(targetX, targetY).addEntity(this);
            setLastMoveTime(getMoveInterval());
        }
    }

    /**
     * Triggers interaction with another Interactable.
     *
     * @param other             The entity to interact with.
     * @param isCellInteraction Indicates whether it's a direct cell interaction.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this, true);
    }
}
