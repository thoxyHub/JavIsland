package com.group16.view.graphics.entity.elements;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.utils.maths.Vector;
import com.group16.view.graphics.entity.EntityView;

/**
 * View class responsible for rendering a WoodBlock entity in the game.
 */
public class WoodBlockView extends EntityView {

    // Path to the wood block image sprite
    private static final String IMAGE_PATH = "/sprites/Outdoor_decoration/Wood_Block.png";

    // Region of Interest selects the portion of the image used for the wood block.
    private static final RegionOfInterest ROI = new RegionOfInterest(45, 45, 410, 410); // replace if needed

    /**
     * Constructs a WoodBlockView at the given world position.
     *
     * @param initialPos The tile-based position of the wood block in the world.
     */
    public WoodBlockView(Vector initialPos) {
        super(new Sprite(IMAGE_PATH, ROI), initialPos);
    }
}
