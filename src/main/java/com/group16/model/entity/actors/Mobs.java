package com.group16.model.entity.actors;

import com.group16.model.area.Island;
import com.group16.model.area.Orientation;
import com.group16.model.entity.Interactable;
import com.group16.model.entity.Interactor;
import com.group16.model.items.weapons.Projectiles;
import com.group16.model.items.weapons.Sword;
import com.group16.model.area.tiles.Tile;
import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;

import java.util.*;

/**
 * Represents an enemy Mob in the game. Mobs are hostile entities that move toward the player and attack them.
 * They use a basic BFS pathfinding algorithm to find a path to the player.
 */
public class Mobs extends Actors implements Interactable, Interactor {

    private static final int DAMAGE = 10;
    private static final int MOB_HEALTH = 150;
    private static final float MOB_SPEED = 4E8f;

    private final Actors target;

    /**
     * Constructs a new Mob targeting a specific player.
     *
     * @param island           The island the mob belongs to
     * @param startingPosition The starting position of the mob
     * @param target           The target actor, usually the player
     */
    public Mobs(Island island, Vector startingPosition, Player target) {
        super(island, startingPosition, MOB_SPEED, MOB_HEALTH);
        this.target = target;
        setState(State.WALK);
    }

    /**
     * Updates the mob’s state. Handles chasing the player using BFS, attacking, or turning to face them.
     *
     * @param deltaTime Time since last update (in nanoseconds)
     */
    @Override
    public void update(float deltaTime) {
        if (target.getHealth() <= 0) return;
        super.update(deltaTime);

        if (getLastMoveTime() < 0) {
            Vector mobPosition = getPosition();

            int playerX = (int) target.getPosition().x();
            int playerY = (int) target.getPosition().y();
            int mobX    = (int) mobPosition.x();
            int mobY    = (int) mobPosition.y();

            int dx = playerX - mobX;
            int dy = playerY - mobY;

            // Check if the mob is adjacent to the player
            if ((Math.abs(dx) == 1 && dy == 0) || (dx == 0 && Math.abs(dy) == 1)) {
                Orientation faceDirection = (Math.abs(dx) == 1)
                        ? (dx > 0 ? Orientation.EAST : Orientation.WEST)
                        : (dy > 0 ? Orientation.SOUTH : Orientation.NORTH);

                if (getCurrentOrientation() != faceDirection) {
                    // Turn to face the player
                    move(faceDirection);
                } else {
                    // Already facing the player – attack
                    Tile t = island.getTile(playerX, playerY);
                    if (t.getEntity() instanceof Player p) {
                        interactWith(p, true);
                    }
                }
            } else {
                // Use BFS to pathfind toward the player
                Orientation step = findNextStepToAdjacent(mobX, mobY, playerX, playerY);
                if (step != null) move(step);
            }

            setLastMoveTimeToMoveInterval();
        }
    }

    /**
     * Finds the first step toward the tile adjacent to the player using BFS.
     *
     * @param mobX     mob’s X coordinate
     * @param mobY     mob’s Y coordinate
     * @param playerX  player’s X coordinate
     * @param playerY  player’s Y coordinate
     * @return Orientation for the next step, or random if no path
     */
    private Orientation findNextStepToAdjacent(int mobX, int mobY, int playerX, int playerY) {
        int width = island.getMapWidth();
        int height = island.getMapHeight();
        boolean[][] seen = new boolean[width][height];
        Map<Vector, Vector> parent = new HashMap<>();
        Queue<Vector> toExplore = new LinkedList<>();

        Vector start = new Vector(mobX, mobY);
        seen[mobX][mobY] = true;
        toExplore.add(start);

        while (!toExplore.isEmpty()) {
            Vector nextTile = toExplore.poll();
            int x = (int) nextTile.x();
            int y = (int) nextTile.y();

            // If adjacent to player, trace back the first move
            if ((Math.abs(x - playerX) == 1 && y == playerY) || (x == playerX && Math.abs(y - playerY) == 1)) {
                Vector currentTile = nextTile;
                Vector parentTile = parent.get(currentTile);
                while (!parentTile.equals(start)) {
                    currentTile = parentTile;
                    parentTile = parent.get(currentTile);
                }

                int dx = (int) currentTile.x() - mobX;
                int dy = (int) currentTile.y() - mobY;
                if (dx > 0) return Orientation.EAST;
                if (dx < 0) return Orientation.WEST;
                if (dy > 0) return Orientation.SOUTH;
                if (dy < 0) return Orientation.NORTH;
            }

            for (Orientation orientation : Orientation.values()) {
                int neighborX = x + (int) (orientation.toVector().x() / GameConfig.TILE_SIZE);
                int neighborY = y + (int) (orientation.toVector().y() / GameConfig.TILE_SIZE);

                if (neighborX < 0 || neighborY < 0 || neighborX >= width || neighborY >= height) continue;
                if (seen[neighborX][neighborY]) continue;

                Tile t = island.getTile(neighborX, neighborY);
                if (!t.isWalkable()) continue;

                Vector validNeighbor = new Vector(neighborX, neighborY);
                seen[neighborX][neighborY] = true;
                parent.put(validNeighbor, nextTile);
                toExplore.add(validNeighbor);
            }
        }

        // Fall back to random movement
        return moveRandomly(mobX, mobY);
    }

    /**
     * Chooses a random walkable direction when no path to the player is found.
     *
     * @param mobX X position
     * @param mobY Y position
     * @return A valid random orientation or null if blocked
     */
    private Orientation moveRandomly(int mobX, int mobY) {
        List<Orientation> possibleMoves = new ArrayList<>();

        for (Orientation orientation : Orientation.values()) {
            int neighborX = mobX + (int) (orientation.toVector().x() / GameConfig.TILE_SIZE);
            int neighborY = mobY + (int) (orientation.toVector().y() / GameConfig.TILE_SIZE);

            if (neighborX < 0 || neighborY < 0 || neighborX >= island.getMapWidth() || neighborY >= island.getMapHeight()) {
                continue;
            }

            Tile t = island.getTile(neighborX, neighborY);
            if (t.isWalkable()) {
                possibleMoves.add(orientation);
            }
        }

        if (possibleMoves.isEmpty()) {
            return null;
        } else {
            int randomIndex = new Random().nextInt(possibleMoves.size());
            return possibleMoves.get(randomIndex);
        }
    }

    /**
     * Gets the fixed damage this mob deals to the player.
     *
     * @return damage value
     */
    public int getDamage() {
        return DAMAGE;
    }

    /*//////////////////////////////////////////////////////////////
                              INTERACTIONS
    //////////////////////////////////////////////////////////////*/

    /**
     * Initiates interaction with another interactable entity.
     *
     * @param other             The target entity
     * @param isCellInteraction Whether the interaction is from the same cell
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this, isCellInteraction);
    }

    /**
     * Accepts interaction from other entities and applies appropriate damage.
     *
     * @param i                 The interactor
     * @param isCellInteraction Whether the interaction is from the same cell
     */
    @Override
    public void acceptInteraction(Interactor i, boolean isCellInteraction) {
        int damage = 0;

        if (i instanceof Player player) {
            damage = player.getCloseRangeDamage();
        } else if (i instanceof Projectiles projectile) {
            damage = projectile.getDamage();
        } else if (i instanceof Sword sword) {
            damage = sword.getDamage();
        }

        if (damage > 0) {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
