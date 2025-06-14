package com.group16.model.entity.actors;

import com.group16.model.entity.GameEntity;
import com.group16.model.area.Island;
import com.group16.model.area.Orientation;
import com.group16.controller.config.GameConfig;
import com.group16.controller.Updatable;
import com.group16.model.utils.maths.Vector;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;

/**
 * Abstract class representing all moving and interacting entities in the game world,
 * such as players and mobs.
 */
public abstract class Actors extends GameEntity implements Updatable, Interactor, Interactable {

    /** The direction the actor is currently facing. */
    private Orientation orientation;

    /** Reference to the island on which the actor exists. */
    protected final Island island;

    /** Time until the actor can perform another move. */
    private float lastMoveTime;

    /** Fixed interval between allowed movements. */
    private final float MOVE_INTERVAL;

    /** Current animation or logical state (idle, walking, attacking). */
    private State state;

    /**
     * Constructs an Actor with given position, movement interval and health.
     *
     * @param island          the island this actor exists on
     * @param startingPosition the initial position
     * @param moveInterval     time between moves in nanoseconds
     * @param health           initial health
     */
    public Actors(Island island, Vector startingPosition, float moveInterval, int health) {
        super(startingPosition, health);
        this.island = island;
        this.MOVE_INTERVAL = moveInterval;
        this.orientation = Orientation.SOUTH;
    }

    /**
     * Attempts to move the actor in the given direction if the target tile is walkable.
     * Updates tile occupancy and orientation.
     *
     * @param direction the direction to move
     */
    public void move(Orientation direction) {
        this.orientation = direction;
        int currentTileX = (int) getPosition().x();
        int currentTileY = (int) getPosition().y();

        int targetTileX = (int) (currentTileX + this.orientation.toVector().x() / GameConfig.TILE_SIZE);
        int targetTileY = (int) (currentTileY + this.orientation.toVector().y() / GameConfig.TILE_SIZE);

        if (this.island.getTile(targetTileX, targetTileY).isWalkable()) {
            setPosition(getPosition().add(
                    this.orientation.toVector().x() / GameConfig.TILE_SIZE,
                    this.orientation.toVector().y() / GameConfig.TILE_SIZE
            ));
            this.island.getTile(currentTileX, currentTileY).removeEntity();
            this.island.getTile(targetTileX, targetTileY).addEntity(this);
        }
        notifyObserver();
    }

    /**
     * Called every frame to update the actor's cooldowns and animations.
     *
     * @param deltaTime time passed since last update (in nanoseconds)
     */
    @Override
    public void update(float deltaTime) {
        if (getHealth() <= 0) return;
        this.lastMoveTime -= deltaTime;
    }

    /*//////////////////////////////////////////////////////////////
                                GETTERS
    //////////////////////////////////////////////////////////////*/

    /**
     * Returns the tile in front of the actor, based on current orientation.
     *
     * @return Vector representing the field of view tile
     */
    public Vector getFieldOfViewCells() {
        return getPosition().add(
                new Vector(
                        orientation.toVector().x() / GameConfig.TILE_SIZE,
                        orientation.toVector().y() / GameConfig.TILE_SIZE
                )
        );
    }

    /**
     * Gets the actor's current orientation.
     *
     * @return current orientation
     */
    public Orientation getCurrentOrientation() {
        return orientation;
    }

    /**
     * Gets the remaining cooldown before the actor can move again.
     *
     * @return time in nanoseconds
     */
    public float getLastMoveTime() {
        return this.lastMoveTime;
    }

    /**
     * Gets the actor's current logical or animation state.
     *
     * @return current state
     */
    public State getState() {
        return state;
    }

    /*//////////////////////////////////////////////////////////////
                                SETTERS
    //////////////////////////////////////////////////////////////*/

    /**
     * Resets the movement timer to the defined interval,
     * enabling regulated movement frequency.
     */
    protected void setLastMoveTimeToMoveInterval() {
        this.lastMoveTime = MOVE_INTERVAL;
    }

    /**
     * Sets the actor's facing direction.
     *
     * @param orientation new orientation
     */
    protected void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        notifyObserver();
    }

    /**
     * Sets the actor's current animation or logical state.
     *
     * @param state new state
     */
    public void setState(State state) {
        this.state = state;
        notifyObserver();
    }
}
