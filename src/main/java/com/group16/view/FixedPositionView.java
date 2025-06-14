package com.group16.view;

import com.group16.view.graphics.Drawable;

import java.awt.*;

/**
 * A drawable sprite that is rendered at a fixed position on the screen with a specified scale.
 */
public class FixedPositionView implements Drawable {

    private final Sprite sprite;
    private final int posOnScreenX;
    private final int posOnScreenY;
    private final int scale;

    public FixedPositionView(Sprite sprite, int posOnScreenX, int posOnScreenY, int scale) {
        this.sprite = sprite;
        this.posOnScreenX = posOnScreenX;
        this.posOnScreenY = posOnScreenY;
        this.scale = scale;
    }

    @Override
    public void draw(Graphics2D g2) {
        final int roiWidth = sprite.getROI().width();
        final int roiHeight = sprite.getROI().height();

        int width = roiWidth * scale;
        int height = roiHeight * scale;

        g2.drawImage(sprite.getImage(), posOnScreenX, posOnScreenY, width, height, null);
    }
}
