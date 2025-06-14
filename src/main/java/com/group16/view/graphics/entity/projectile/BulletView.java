package com.group16.view.graphics.entity.projectile;

import com.group16.model.utils.maths.Vector;
import com.group16.model.area.Orientation;
import com.group16.model.items.weapons.Bullet;
import com.group16.controller.config.GameConfig;
import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.Subject;
import com.group16.view.utils.ImageManipulation;
import com.group16.view.graphics.entity.EntityView;

import java.awt.*;

/**
 * View class responsible for rendering a bullet projectile on screen.
 */
public class BulletView extends EntityView {

    // Path to the bullet sprite image
    private static final String Bullet_IMAGE = "/sprites/FireBullet.png";

    // Defines which part of the sprite sheet is used for the bullet
    private static final RegionOfInterest ROI = new RegionOfInterest(336, 0, 16, 16);

    // Sprites for each bullet orientation
    private static final Sprite eastSprite = new Sprite(Bullet_IMAGE, ROI);
    private static final Sprite westSprite = new Sprite(ImageManipulation.flipImage(eastSprite.getImage()));
    private static final Sprite southSprite = new Sprite(ImageManipulation.rotateImage90Clockwise(eastSprite.getImage()));
    private static final Sprite northSprite = new Sprite(ImageManipulation.rotateImage90Clockwise(westSprite.getImage()));

    /**
     * Constructs the BulletView at the initial position. Default sprite is facing east.
     *
     * @param initialPos The initial position of the bullet.
     */
    public BulletView(Vector initialPos) {
        super(eastSprite, initialPos); // Starts with east-facing sprite
    }

    /**
     * Draws the bullet on screen relative to the camera position.
     */
    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        int drawX = posX * GameConfig.TILE_SIZE - cameraX;
        int drawY = posY * GameConfig.TILE_SIZE - cameraY;

        g2.drawImage(
                sprite.getImage(),
                drawX,
                drawY,
                GameConfig.TILE_SIZE,
                GameConfig.TILE_SIZE,
                null
        );
    }

    /**
     * Updates the view when the Bullet model changes.
     */
    @Override
    public void update(Subject s) {
        if (!(s instanceof Bullet bullet)) return;

        // Update position based on model
        posX = (int) bullet.getPosition().x();
        posY = (int) bullet.getPosition().y();

        // Select appropriate sprite based on orientation
        Orientation orientation = bullet.getOrientation();

        switch (orientation) {
            case NORTH -> this.sprite = northSprite;
            case SOUTH -> this.sprite = southSprite;
            case EAST  -> this.sprite = eastSprite;
            case WEST  -> this.sprite = westSprite;
        }
    }
}
