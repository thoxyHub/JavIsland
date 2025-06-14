package com.group16.view.graphics.area;

import com.group16.model.entity.actors.Player;
import com.group16.controller.config.GameConfig;

/**
 * Camera class abstraction responsible for calculating the offset needed to center the view on the player.
 */
public class Camera {

    private final Player player;

    /**
     * Constructs a Camera that follows the given player.
     *
     * @param player the player to follow
     */
    public Camera(Player player) {
        this.player = player;
    }

    /**
     * Calculates the horizontal offset required to center the player on screen.
     *
     * @return the X-axis offset in pixels
     */
    public int getOffsetX() {
        return (int) (player.getPosition().x() * GameConfig.TILE_SIZE - GameConfig.PANEL_CENTER.x());
    }

    /**
     * Calculates the vertical offset required to center the player on screen.
     *
     * @return the Y-axis offset in pixels
     */
    public int getOffsetY() {
        return (int) (player.getPosition().y() * GameConfig.TILE_SIZE - GameConfig.PANEL_CENTER.y());
    }

}
