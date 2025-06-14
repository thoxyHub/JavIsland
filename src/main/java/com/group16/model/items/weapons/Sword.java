package com.group16.model.items.weapons;

import com.group16.model.entity.GameEntity;
import com.group16.model.area.Orientation;
import com.group16.model.area.Island;
import com.group16.model.entity.elements.Element;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.items.Inventory;
import com.group16.model.area.tiles.Tile;
import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;

import java.util.function.Supplier;

/**
 * Represents a close-range weapon (sword) that can damage or farm entities directly in front of the player.
 */
public class Sword extends Weapon implements Interactor {

    // The island context for performing interactions
    private final Island island;

    // Stores the entity that the sword will interact with (used in attack)
    private GameEntity targetEntity;

    /**
     * Constructs a Sword object.
     *
     * @param damage               damage dealt by the sword
     * @param posSupplier          supplier for the owner's position
     * @param orientationSupplier  supplier for the owner's orientation
     * @param island               reference to the island (map) where interactions occur
     */
    public Sword(int damage,
                 Supplier<Vector> posSupplier,
                 Supplier<Orientation> orientationSupplier,
                 Island island) {
        super(damage, posSupplier, orientationSupplier);
        this.island = island;
    }

    /**
     * Uses the sword: finds an entity in front of the player and interacts with it.
     *
     * @param inventory the player's inventory (used for farming rewards)
     */
    @Override
    public void use(Inventory inventory) {
        Vector currentPosition = ownerPositionSupplier.get();
        Orientation orientation = ownerOrientationSupplier.get();

        int currX = (int) currentPosition.x();
        int currY = (int) currentPosition.y();
        int targetX = (int) (currX + orientation.toVector().x() / GameConfig.TILE_SIZE);
        int targetY = (int) (currY + orientation.toVector().y() / GameConfig.TILE_SIZE);

        // Check if the target is within map boundaries
        if (targetX < 0 || targetY < 0 || targetX >= island.getMapWidth() || targetY >= island.getMapHeight()) {
            System.out.println("Target out of bounds");
            return;
        }

        Tile targetTile = island.getTile(targetX, targetY);
        GameEntity entity = targetTile.getEntity();

        if (entity == null) {
            System.out.println("No entity to interact with");
            return;
        }

        // Save the context for attack
        this.targetEntity = entity;

        attack(inventory);
    }

    /**
     * Attacks the previously targeted entity, applying damage and farming resources if applicable.
     *
     * @param inventory the player's inventory
     */
    @Override
    public void attack(Inventory inventory) {
        if (targetEntity instanceof Interactable interactable) {
            interactable.acceptInteraction(this, true);
            if (interactable instanceof Element element) {
                element.farm(inventory);
            }
        }
    }

    /**
     * Performs an interaction with a given Interactable entity.
     *
     * @param other             the entity to interact with
     * @param isCellInteraction true if interacting with the tile's content
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this, isCellInteraction);
    }
}
