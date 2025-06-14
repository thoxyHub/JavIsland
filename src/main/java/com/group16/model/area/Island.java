package com.group16.model.area;

import com.group16.controller.Updatable;
import com.group16.model.entity.GameEntity;
import com.group16.model.entity.actors.Mobs;
import com.group16.model.entity.actors.Player;
import com.group16.model.area.tiles.Tile;
import com.group16.model.utils.maths.Vector;
import com.group16.model.Subject;
import com.group16.view.Observer;

import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * Represents the game island which manages all tiles, entities, and game updates.
 * Acts as the main environment for players, mobs, and interactions.
 */
public class Island implements Subject {

    /**
     * Enum representing the type of change occurring on the island (e.g. adding or removing an entity).
     */
    public enum ChangeType {
        ADD, REMOVE
    }

    private ChangeType lastChangeType;
    private GameEntity lastChangedEntity;

    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    private final int MAP_BORDER = 7; // width of the water border

    private final Tile[][] tileMap;

    private final List<GameEntity> gameEntities = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    private boolean areMobsAlive;

    /**
     * Constructs the island using a tile map.
     *
     * @param map the 2D array of tiles that make up the island
     * @throws IOException if an issue occurs during initialization
     */
    public Island(Tile[][] map) throws IOException {
        Objects.requireNonNull(map);

        tileMap = map;
        MAP_WIDTH = tileMap[0].length;
        MAP_HEIGHT = tileMap.length;
    }

    /**
     * Adds an entity to the island and notifies observers.
     *
     * @param entity the game entity to add
     */
    public void addEntity(GameEntity entity) {
        gameEntities.add(entity);
        lastChangeType = ChangeType.ADD;
        lastChangedEntity = entity;
        notifyObserver();
    }

    /**
     * Removes an entity from the island and notifies observers.
     *
     * @param entity the game entity to remove
     */
    public void removeEntity(GameEntity entity) {
        gameEntities.remove(entity);
        lastChangeType = ChangeType.REMOVE;
        lastChangedEntity = entity;
        notifyObserver();
    }

    /**
     * Returns a tile based on x and y coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the corresponding tile
     */
    public Tile getTile(int x, int y) {
        return getTile(new Vector(x, y));
    }

    /**
     * Returns a tile at a given position vector.
     *
     * @param position the position vector
     * @return the corresponding tile
     */
    public Tile getTile(Vector position) {
        int x = (int) position.x();
        int y = (int) position.y();
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds");
        }
        return tileMap[y][x];
    }

    public int getMapWidth() {
        return MAP_WIDTH;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    /**
     * Adds a player to the island and sets their position on the tile map.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        Objects.requireNonNull(player);

        addEntity(player);

        int x = (int) player.getPosition().x();
        int y = (int) player.getPosition().y();

        tileMap[y][x].addEntity(player);
    }

    public int getMAP_HEIGHT() {
        return MAP_HEIGHT;
    }

    public int getMAP_WIDTH() {
        return MAP_WIDTH;
    }

    public int getMAP_BORDER() {
        return MAP_BORDER;
    }

    /**
     * Called at the start of a new wave to re-place entities on their respective tiles.
     */
    public void updateNewRound() {
        for (GameEntity entity : gameEntities) {
            int x = (int) entity.getPosition().x();
            int y = (int) entity.getPosition().y();

            if (!(entity instanceof Player)){
                tileMap[y][x].addEntity(entity);
            }
        }
    }

    /**
     * Updates all game entities and handles their removal if dead.
     *
     * @param deltaTime time elapsed since the last update
     */
    public void update(float deltaTime) {
        areMobsAlive = false;

        // Update the entities in the island
        for (GameEntity entity : new ArrayList<>(gameEntities)) {

            if (entity.getHealth() <= 0) {
                int x = (int) entity.getPosition().x();
                int y = (int) entity.getPosition().y();

                this.getTile(x, y).removeEntity();
                removeEntity(entity);
            } else {
                if (entity instanceof Updatable) {
                    ((Updatable) entity).update(deltaTime);
                }

                if (entity instanceof Mobs) {
                    areMobsAlive = true;
                }
            }
        }
    }

    /**
     * @return true if at least one mob is still alive
     */
    public boolean stillMobsAlive() {
        return areMobsAlive;
    }

    public ChangeType getLastChangeType() {
        return lastChangeType;
    }

    public GameEntity getLastChangedEntity() {
        return lastChangedEntity;
    }

    /**
     * Clears the last tracked change (used after notifying observers).
     */
    public void clearLastChange() {
        lastChangedEntity = null;
        lastChangeType = null;
    }

    @Override
    public void notifyObserver() {
        observers.forEach(o -> o.update(this));
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
}
