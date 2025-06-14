package com.group16.view.graphics.entity;

import com.group16.model.utils.maths.Vector;
import com.group16.model.entity.GameEntity;
import com.group16.controller.config.GameConfig;
import com.group16.view.graphics.PositionedDrawable;
import com.group16.model.Subject;
import com.group16.view.Sprite;
import com.group16.view.Observer;

import java.awt.*;

/**
 * Generic view class for rendering any GameEntity on the screen.
 * Acts as a base class for more specific entity views.
 */
public class EntityView implements Observer, PositionedDrawable {

    protected Sprite sprite;
    protected int posX, posY;

    /**
     * Constructs a visual representation of an entity at a given position.
     *
     * @param sprite           The sprite image to display.
     * @param initialPosition  The initial position of the entity.
     */
    public EntityView(Sprite sprite, Vector initialPosition) {
        this.sprite = sprite;
        this.posX = (int) initialPosition.x();
        this.posY = (int) initialPosition.y();
    }

    /**
     * Draws the entity's sprite at its current position, adjusted by the camera offset.
     *
     * @param g2       The graphics context.
     * @param cameraX  The X offset of the camera.
     * @param cameraY  The Y offset of the camera.
     */
    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        if (sprite.getImage() == null) {
            return;
        }

        g2.drawImage(
                sprite.getImage(),
                posX * GameConfig.TILE_SIZE - cameraX,
                posY * GameConfig.TILE_SIZE - cameraY,
                GameConfig.TILE_SIZE,
                GameConfig.TILE_SIZE,
                null
        );
    }

    /**
     * Updates the visual position based on the GameEntity's current position.
     *
     * @param s The subject (must be a GameEntity).
     */
    @Override
    public void update(Subject s) {
        // Ensure the subject is a GameEntity before updating
        assert (s instanceof GameEntity);

        GameEntity e = (GameEntity) s;

        posX = (int) e.getPosition().x();
        posY = (int) e.getPosition().y();
    }
}
