package com.group16.view.graphics.area;

import com.group16.model.entity.GameEntity;
import com.group16.model.area.Island;
import com.group16.controller.config.GameConfig;
import com.group16.controller.Updatable;
import com.group16.model.Subject;
import com.group16.view.graphics.Drawable;
import com.group16.view.Observer;
import com.group16.view.graphics.entity.EntityView;
import com.group16.view.graphics.entity.EntityViewFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WorldView handles the rendering of the entire game world including background tiles and all entities.
 * It observes the Island model for entity changes and maintains a corresponding view map.
 */
public class WorldView implements Observer, Drawable, Updatable {

    private final Camera camera;

    // Maps each GameEntity to its corresponding visual representation
    private final Map<GameEntity, EntityView> entityViewMap = new HashMap<>();

    // World dimensions and tiles
    private final int worldHeight;
    private final int worldWidth;
    private final TileView[][] tileWorld;

    /**
     * Constructs the WorldView with a camera and tile layout.
     *
     * @param camera     the camera responsible for translating world to screen coordinates
     * @param tileWorld  the 2D tile layout of the world
     */
    public WorldView(Camera camera, TileView[][] tileWorld) {
        this.camera = camera;
        this.tileWorld = tileWorld;
        this.worldHeight = tileWorld.length;
        this.worldWidth = tileWorld[0].length;
    }

    /**
     * Draws all entities in the game world. Entities are sorted by Y-coordinate
     * to ensure correct rendering order (lower entities drawn last).
     *
     * @param g2 the graphics context
     */
    public void drawEntities(Graphics2D g2) {
        List<Map.Entry<GameEntity, EntityView>> entries = new ArrayList<>(entityViewMap.entrySet());

        // Sort entities by vertical position (Y-axis)
        entries.sort((entry1, entry2) ->
                Float.compare(
                        entry1.getKey().getPosition().y(),
                        entry2.getKey().getPosition().y()
                )
        );

        for (Map.Entry<GameEntity, EntityView> entry : entries) {
            entry.getValue().draw(g2, camera.getOffsetX(), camera.getOffsetY());
        }
    }

    /**
     * Draws the background tile map.
     *
     * @param g2 the graphics context
     */
    private void drawTiles(Graphics2D g2) {
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                TileView tile = tileWorld[y][x];
                tile.draw(
                        g2,
                        x * GameConfig.TILE_SIZE - camera.getOffsetX(),
                        y * GameConfig.TILE_SIZE - camera.getOffsetY()
                );
            }
        }
    }

    /**
     * Observer callback when the Island model changes (e.g., entities are added or removed).
     *
     * @param s the subject (should be of type Island)
     */
    @Override
    public void update(Subject s) {
        if (!(s instanceof Island island)) return;

        GameEntity entity = island.getLastChangedEntity();
        Island.ChangeType change = island.getLastChangeType();

        switch (change) {
            case ADD -> {
                EntityView view = EntityViewFactory.createView(entity, entity.getPosition());
                entity.registerObserver(view);
                entityViewMap.put(entity, view);
            }
            case REMOVE -> entityViewMap.remove(entity);
        }

        island.clearLastChange();
    }

    /**
     * Updates the visual representation of entities that are themselves updatable.
     *
     * @param deltaTime the time passed since last frame in nanoseconds
     */
    @Override
    public void update(float deltaTime) {
        entityViewMap.forEach((entity, view) -> {
            if (view instanceof Updatable) {
                ((Updatable) view).update(deltaTime);
            }
        });
    }

    /**
     * Draws the full world view: tiles followed by entities.
     *
     * @param g2 the graphics context
     */
    @Override
    public void draw(Graphics2D g2) {
        drawTiles(g2);
        drawEntities(g2);
    }
}
