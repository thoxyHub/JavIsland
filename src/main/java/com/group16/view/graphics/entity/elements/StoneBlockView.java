package com.group16.view.graphics.entity.elements;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.utils.maths.Vector;
import com.group16.view.graphics.entity.EntityView;

/**
 * Visual representation of a stone block placed in the game world.
 * Handles sprite rendering for stone block entities.
 */
public class StoneBlockView extends EntityView {

    // Path to the sprite image used for rendering the stone block
    private static final String IMAGE_PATH = "/sprites/Outdoor_decoration/RockBlock.png";

    // Region of Interest (ROI) from the image: selects the 160x160 region starting at (0, 0)
    // Adjust ROI if using a different portion or sprite sheet
    private static final RegionOfInterest ROI = new RegionOfInterest(42, 35, 416, 436); // replace if needed

    /**
     * Constructs a StoneBlockView at the specified position.
     *
     * @param initialPos The initial position of the stone block in game coordinates.
     */
    public StoneBlockView(Vector initialPos) {
        super(new Sprite(IMAGE_PATH, ROI), initialPos);
    }
}
