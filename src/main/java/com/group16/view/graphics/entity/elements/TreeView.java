package com.group16.view.graphics.entity.elements;

import com.group16.controller.config.GameConfig;
import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.utils.maths.Vector;
import com.group16.view.graphics.entity.EntityView;

import java.awt.*;

/**
 * View class responsible for rendering a tree entity in the game.
 * Applies offset corrections to align the tree sprite visually on the map.
 */
public class TreeView extends EntityView {

    // Path to the tree sprite image
    private static final String TREE_IMAGE = "/sprites/Outdoor_decoration/Oak_Tree.png";

    // Region of Interest selects the portion of the image used as the tree sprite (64x80 pixels)
    private static final RegionOfInterest ROI_IMAGE = new RegionOfInterest(0, 0, 64, 80);

    /**
     * Constructs a TreeView at the given world position.
     *
     * @param initialPos The position of the tree in game tile coordinates.
     */
    public TreeView(Vector initialPos) {
        super(new Sprite(TREE_IMAGE, ROI_IMAGE), initialPos);
    }

    /**
     * Draws the tree sprite at the specified screen position, offset to center visually.
     * The X and Y offsets (-7 and -15 scaled units) adjust the sprite to appear properly
     * anchored on the tile grid, due to its larger height and width.
     *
     * @param g2       The Graphics2D context used for rendering.
     * @param cameraX  X offset of the camera.
     * @param cameraY  Y offset of the camera.
     */
    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        int drawX = posX * GameConfig.TILE_SIZE - cameraX - 7 * GameConfig.SCALE;
        int drawY = posY * GameConfig.TILE_SIZE - cameraY - 15 * GameConfig.SCALE;

        int width = sprite.getROI().width() * GameConfig.SCALE / 2;
        int height = sprite.getROI().height() * GameConfig.SCALE / 2;

        g2.drawImage(sprite.getImage(), drawX, drawY, width, height, null);
    }
}
