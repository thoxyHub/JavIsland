package com.group16.view;

import com.group16.view.utils.RegionOfInterest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents a sprite, which is a portion of an image (texture) defined by a {@link RegionOfInterest}.
 * Sprites are used for rendering game elements such as tiles, characters, and items.
 */
public class Sprite {
    private final BufferedImage sprite;
    private final RegionOfInterest ROI;

    /**
     * Constructs a sprite from an image file and a region of interest (ROI) within that image.
     *
     * @param addressImage the path to the image resource (must be on the classpath)
     * @param ROI          the region of the image to use as the sprite
     * @throws RuntimeException if the image cannot be loaded or the path is invalid
     */
    public Sprite(String addressImage, RegionOfInterest ROI) {
        this.ROI = ROI;
        BufferedImage fullImage = null;

        try {
            fullImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(addressImage)));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to load image: " + addressImage, e);
        }

        if (fullImage == null) {
            throw new IllegalArgumentException("Image not found: " + addressImage);
        }

        this.sprite = fullImage.getSubimage(ROI.x1(), ROI.y1(), ROI.width(), ROI.height());
    }

    /**
     * Constructs a sprite directly from a {@link BufferedImage}.
     *
     * @param sprite the image to use as the sprite
     */
    public Sprite(BufferedImage sprite) {
        this.sprite = sprite;
        ROI = new RegionOfInterest(sprite.getMinX(), sprite.getMinY(), sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Returns the image used by this sprite.
     *
     * @return the buffered image representing this sprite
     */
    public BufferedImage getImage() {
        return sprite;
    }

    /**
     * Returns the region of interest used to extract this sprite.
     *
     * @return a copy of the ROI used for the sprite
     */
    public RegionOfInterest getROI() {
        return new RegionOfInterest(ROI.x1(), ROI.y1(), ROI.width(), ROI.height());
    }
}
