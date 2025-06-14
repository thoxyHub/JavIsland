package com.group16.model.entity.actors;

import com.group16.model.area.Island;
import com.group16.model.entity.GameEntity;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.items.resources.*;
import com.group16.model.items.weapons.Gun;
import com.group16.model.area.Orientation;
import com.group16.model.entity.elements.Element;
import com.group16.model.items.Inventory;
import com.group16.model.items.weapons.Sword;
import com.group16.model.area.tiles.Tile;
import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;

/**
 * Represents the player character in the game.
 * Inherits from {@link Actors} and contains movement, interaction, and combat logic.
 */
public final class Player extends Actors {
    private final Gun gun;
    private final Sword sword;
    private final Inventory inventory;

    private boolean isWalking;

    /** Damage dealt when attacking in close range */
    private static final int CLOSE_RANGE_DAMAGE = 50;

    /** Cooldown duration between gun shots */
    private final float SHOOT_COOLDOWN = 3e8f;

    /** Timer tracking time until player can shoot again */
    private float lastShootTime = 0;

    /** Duration of an attack animation */
    private static final float ATTACK_DURATION = 2e8f;

    /** Timer for how long the attack lasts */
    private float attackTimeRemaining = 0f;

    /** How long a movement key has been pressed */
    private float movePressTime = 0;

    public static final int PLAYER_MAX_HEALTH = 300;
    private static final float PLAYER_SPEED = 5E3f;

    /**
     * Constructs the player with weapons and starting resources.
     * @param island The island the player is on
     * @param startingPosition The initial position of the player
     */
    public Player(Island island, Vector startingPosition) {
        super(island, startingPosition, PLAYER_SPEED, PLAYER_MAX_HEALTH);

        // Initialize inventory and starter resources
        this.inventory = new Inventory();
        this.inventory.add(new Resources(ResourceType.AMMO, 10));
        this.inventory.add(new Resources(ResourceType.WOOD, 5));
        this.inventory.add(new Resources(ResourceType.STONE, 5));

        // Create and configure gun
        gun = new Gun(
                50,
                this::getPosition,
                this::getCurrentOrientation,
                bullet -> {
                    bullet.setIsland(this.island);
                    Vector pos = bullet.getPosition();
                    int x = (int) pos.x();
                    int y = (int) pos.y();

                    if (island.getTile(x, y).getEntity() == null) {
                        island.addEntity(bullet);
                        island.getTile(x, y).addEntity(bullet);
                    } else {
                        GameEntity target = island.getTile(x, y).getEntity();
                        if (target instanceof Interactable i) {
                            bullet.interactWith(i, true);
                        }
                    }
                }
        );

        // Create and configure sword
        sword = new Sword(
                50,
                this::getPosition,
                this::getCurrentOrientation,
                this.island
        );

        // Add weapons to inventory and assign to pocket slots
        this.inventory.add(sword);
        this.inventory.setPocketItem(0, sword);
        this.inventory.add(gun);
        this.inventory.setPocketItem(1, gun);
    }

    /**
     * Updates player state each game tick.
     * @param deltaTime Elapsed time since last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        lastShootTime -= deltaTime;
        attackTimeRemaining = Math.max(0, attackTimeRemaining - deltaTime);

        if (getLastMoveTime() < 0) {
            movePressTime += deltaTime;
        }

        // Update animation state
        if (attackTimeRemaining > 0) {
            setState(State.SLASH);
        } else if (isWalking) {
            setState(State.WALK);
        } else {
            setState(State.IDLE);
        }

        isWalking = false;
    }

    /**
     * Adds an element to the tile and registers it with the island.
     * @param tile The tile to place the element on
     * @param element The element to place
     */
    public void build(Tile tile, Element element) {
        tile.addEntity(element);
        island.addEntity(element);
    }

    /** Builds a wood block in front of the player if resources allow */
    public void buildingWoodBlock() {
        Tile currentTile = island.getTile(
                (int) (getPosition().x() + getCurrentOrientation().toVector().x() / GameConfig.TILE_SIZE),
                (int) (getPosition().y() + getCurrentOrientation().toVector().y() / GameConfig.TILE_SIZE));
        currentTile.acceptInteraction(this, true);
        setLastMoveTimeToMoveInterval();
    }

    /** Uses the sword in the direction the player is facing */
    public void useSword() {
        sword.use(inventory);
        attackTimeRemaining = ATTACK_DURATION;
        setLastMoveTimeToMoveInterval();
    }

    /** Uses the gun if it is off cooldown and player has ammo */
    public void useGun() {
        if (lastShootTime <= 0) {
            gun.use(inventory);
            this.lastShootTime = SHOOT_COOLDOWN;
        }
    }

    /** Builds a stone block in front of the player if resources allow */
    public void buildingStoneBlock() {
        Tile currentTile = island.getTile(
                (int) (getPosition().x() + getCurrentOrientation().toVector().x() / GameConfig.TILE_SIZE),
                (int) (getPosition().y() + getCurrentOrientation().toVector().y() / GameConfig.TILE_SIZE));
        this.interactWith(currentTile, false);
        setLastMoveTimeToMoveInterval();
    }

    /** Toggles the inventory open/closed */
    public void openCloseInventory() {
        this.inventory.changeOpenClose();
        setLastMoveTimeToMoveInterval();
    }

    /** Initiates movement north if allowed */
    public void moveNorth() {
        if (getLastMoveTime() < 0) {
            handleMove(Orientation.NORTH);
        }
    }

    /** Initiates movement south if allowed */
    public void moveSouth() {
        if (getLastMoveTime() < 0) {
            handleMove(Orientation.SOUTH);
        }
    }

    /** Initiates movement east if allowed */
    public void moveEast() {
        if (getLastMoveTime() < 0) {
            handleMove(Orientation.EAST);
        }
    }

    /** Initiates movement west if allowed */
    public void moveWest() {
        if (getLastMoveTime() < 0) {
            handleMove(Orientation.WEST);
        }
    }

    /** @return The player's inventory */
    public Inventory getInventory() {
        return inventory;
    }

    /** @return The amount of damage dealt in close combat */
    public int getCloseRangeDamage(){
        return CLOSE_RANGE_DAMAGE;
    }

    /**
     * Handles movement logic and orientation update
     * @param orientation Direction to move or face
     */
    private void handleMove(Orientation orientation) {
        if (orientation.equals(this.getCurrentOrientation())) {
            this.move(orientation);
            setLastMoveTimeToMoveInterval();
            isWalking = true;
        } else {
            this.setOrientation(orientation);
            movePressTime = 0;
        }
    }

    /**
     * Called when player interacts with something
     * @param other Target of interaction
     * @param isCellInteraction Whether the interaction is with the tile or entity
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this, isCellInteraction);
    }

    /**
     * Called when another entity interacts with this player
     * @param i The interactor
     * @param isCellInteraction Whether the interaction is tile-based
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        if (i instanceof Mobs mob) {
            int newHp = getHealth() - mob.getDamage();
            setHealth(newHp);
            if (newHp <= 0) {
                Vector p = getPosition();
                island.getTile((int)p.x(), (int)p.y()).removeEntity();
            }
        }
    }
}
