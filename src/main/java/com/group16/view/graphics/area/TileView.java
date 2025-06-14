package com.group16.view.graphics.area;

import com.group16.controller.config.GameConfig;
import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.graphics.PositionedDrawable;

import java.awt.*;

/**
 * Enum representing the different visual representations of tiles in the game.
 * Each tile view is associated with a specific sprite, which is drawn based on a region of interest.
 */
public enum TileView implements PositionedDrawable {
    // TYPE OF WATER
    WATER_MIDDLE(new Sprite("/sprites/Tiles/Water_Middle.png", new RegionOfInterest(0, 0, 16, 16))),
    WATER_MIDDLE_WAVE(new Sprite("/sprites/Tiles/Water_Middle_Wave.png", new RegionOfInterest(0, 0, 16, 16))),

    // TYPE OF SAND
    SAND_MIDDLE(new Sprite("/sprites/Tiles/Path_Middle.png", new RegionOfInterest(0, 0, 16, 16))),

    // TYPE OF GRASS
    GRASS_MIDDLE(new Sprite("/sprites/Tiles/Grass_Middle.png", new RegionOfInterest(0, 0, 16, 16))),
    GRASS_RED_FLOWER(new Sprite("/sprites/Tiles/Grass_Middle2.png", new RegionOfInterest(0, 0, 16, 16))),
    GRASS_WEED(new Sprite("/sprites/Tiles/Grass_Middle3.png", new RegionOfInterest(0, 0, 16, 16))),
    GRASS_YELLOW_FLOWER(new Sprite("/sprites/Tiles/Grass_Middle4.png", new RegionOfInterest(0, 0, 16, 16))),

    // HILL + the quadrant(s) where the grass is
    HILL_1(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(0, 64, 16, 16))),
    HILL_2(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(16, 64, 16, 16))),
    HILL_3(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(16, 48, 16, 16))),
    HILL_4(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(0, 48, 16, 16))),
    HILL_12(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(16, 0, 16, 16))),
    HILL_14(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(32, 16, 16, 16))),
    HILL_23(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(0, 16, 16, 16))),
    HILL_34(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(16, 32, 16, 16))),
    HILL_123(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(0, 0, 16, 16))),
    HILL_124(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(32, 0, 16, 16))),
    HILL_134(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(32, 32, 16, 16))),
    HILL_234(new Sprite("/sprites/Tiles/Water_Tile.png", new RegionOfInterest(0, 32, 16, 16)));

    private final Sprite sprite;

    TileView(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(Graphics2D g2, int posX, int posY) {
        g2.drawImage(sprite.getImage(), posX, posY, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
