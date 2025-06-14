package com.group16.model.items.weapons;

import com.group16.model.area.Orientation;
import com.group16.model.items.Inventory;
import com.group16.model.items.Usable;
import com.group16.model.utils.maths.Vector;
import com.group16.model.items.resources.Item;

import java.util.function.Supplier;

/**
 * Abstract base class for all weapons in the game.
 * Weapons are items that can be used to deal damage and perform attacks.
 * Implements {@link Usable} and extends {@link Item}.
 */
public abstract class Weapon extends Item implements Usable {

    // The amount of damage the weapon inflicts
    private final int damage;

    // Supplier for the owner's current position (e.g., player)
    protected Supplier<Vector> ownerPositionSupplier;

    // Supplier for the owner's current orientation (e.g., facing direction)
    protected Supplier<Orientation> ownerOrientationSupplier;

    /**
     * Constructs a weapon.
     *
     * @param damage                 the amount of damage this weapon deals
     * @param ownerPositionSupplier supplier providing the position of the weapon's owner
     * @param ownerOrientationSupplier supplier providing the orientation of the weapon's owner
     */
    public Weapon(int damage,
                  Supplier<Vector> ownerPositionSupplier,
                  Supplier<Orientation> ownerOrientationSupplier) {
        super(1); // All weapons have a default quantity of 1
        this.damage = damage;
        this.ownerPositionSupplier = ownerPositionSupplier;
        this.ownerOrientationSupplier = ownerOrientationSupplier;
    }

    /**
     * Returns the damage value of the weapon.
     *
     * @return damage dealt by this weapon
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Performs an attack. Concrete subclasses must define how the attack is carried out.
     *
     * @param inventory the inventory of the entity using the weapon (e.g., for farming or ammo checks)
     */
    public abstract void attack(Inventory inventory);

    /**
     * Default implementation of the use behavior, calling the {@link #attack(Inventory)} method.
     *
     * @param inventory the inventory to be passed to the attack logic
     */
    @Override
    public void use(Inventory inventory) {
        this.attack(inventory);
    }
}
