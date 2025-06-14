package com.group16.view.graphics;

import java.awt.*;

/**
 * Represents a drawable object that can render itself at a specified position
 * using a {@link Graphics2D} context. Useful for objects that need to be drawn
 * at dynamic coordinates within a larger canvas or game world.
 */
public interface PositionedDrawable {
    /**
     * Draws the object at the given position.
     *
     * @param g2   the graphics context used for rendering
     * @param posX the x-coordinate where the object should be drawn
     * @param posY the y-coordinate where the object should be drawn
     */
    void draw(Graphics2D g2, int posX, int posY);
}
