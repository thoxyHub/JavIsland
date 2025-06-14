package com.group16.model.items.weapons;

import com.group16.model.area.Orientation;
import com.group16.model.items.Inventory;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;
import com.group16.model.utils.maths.Vector;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a gun weapon that fires bullets in the given orientation.
 * It consumes ammo from the player's inventory when used.
 */
public class Gun extends Weapon {

    // Functional interface to handle bullet spawning logic (provided externally)
    private final Consumer<Bullet> bulletSpawner;

    /**
     * Constructs a Gun instance.
     *
     * @param damage               the amount of damage each bullet deals
     * @param posSupplier          supplies the current position of the owner (player)
     * @param orientationSupplier  supplies the current orientation of the owner
     * @param bulletSpawner        consumer to define how a bullet is spawned into the game world
     */
    public Gun(int damage,
               Supplier<Vector> posSupplier,
               Supplier<Orientation> orientationSupplier,
               Consumer<Bullet> bulletSpawner) {
        super(damage, posSupplier, orientationSupplier);
        this.bulletSpawner = bulletSpawner;
    }

    /**
     * Uses the gun. If there is enough ammo in the inventory,
     * a bullet is fired and ammo is consumed.
     *
     * @param inventory the inventory from which ammo is deducted
     */
    @Override
    public void use(Inventory inventory) {
        if (inventory.getAmount(new Resources(ResourceType.AMMO, 0)) <= 0) {
            System.out.println("Out of ammo");
        } else {
            // Decrement ammo
            inventory.remove(new Resources(ResourceType.AMMO, 1));
            // Perform attack
            attack(inventory);
        }
    }

    /**
     * Spawns a bullet in the direction the owner is facing.
     *
     * @param inventory the inventory (not used directly here)
     */
    @Override
    public void attack(Inventory inventory) {
        Orientation orientation = ownerOrientationSupplier.get();
        Vector startPos = calculateBulletPosition(orientation);
        Bullet bullet = new Bullet(getDamage(), startPos, orientation);
        bulletSpawner.accept(bullet);
    }

    /**
     * Calculates the starting position of the bullet based on orientation.
     *
     * @param orientation the orientation in which the bullet is fired
     * @return the position one tile ahead of the owner's position
     */
    private Vector calculateBulletPosition(Orientation orientation) {
        Vector pos = ownerPositionSupplier.get();
        return switch (orientation) {
            case NORTH -> pos.add(0, -1);
            case SOUTH -> pos.add(0, 1);
            case EAST -> pos.add(1, 0);
            case WEST -> pos.add(-1, 0);
        };
    }
}
