package com.group16.view.graphics.entity.elements;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.utils.maths.Vector;
import com.group16.view.graphics.entity.EntityView;

import java.awt.*;

/**
 * Visual representation of a rock element on the game map.
 * This class handles the sprite and drawing logic for rocks.
 */
public class RockView extends EntityView {

    // Path to the rock sprite sheet image
    private static final String ROCK_IMAGE = "/sprites/Outdoor_decoration/Outdoor_Decor_Free.png";

    // Region of Interest (ROI) in the sprite sheet: x=0, y=48, width=16, height=16
    // This selects the specific portion of the sprite sheet representing a rock
    private static final RegionOfInterest ROI = new RegionOfInterest(0, 48, 16, 16);

    /**
     * Constructs a RockView with a given initial position.
     *
     * @param initialPos The initial position of the rock in game coordinates.
     */
    public RockView(Vector initialPos) {
        super(new Sprite(ROCK_IMAGE, ROI), initialPos);
    }

    /**
     * Draws the rock sprite using the camera offset.
     *
     * @param g2        The graphics context.
     * @param cameraX   The x-offset of the camera.
     * @param cameraY   The y-offset of the camera.
     */
    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        super.draw(g2, cameraX, cameraY);
    }
}
