package com.group16.view.graphics;

import java.awt.*;

/**
 * Represents a drawable object that can render itself using a {@link Graphics2D} context.
 * Typically implemented by views that need to be drawn to the screen.
 */
public interface Drawable {
    /**
     * Draws the object onto the screen.
     *
     * @param g2 the graphics context used for rendering
     */
    void draw(Graphics2D g2);
}
