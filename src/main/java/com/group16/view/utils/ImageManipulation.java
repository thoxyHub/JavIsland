package com.group16.view.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Utility class for image transformations such as flipping and rotating.
 */
public abstract class ImageManipulation {

    /**
     * Flips the given image horizontally.
     *
     * @param image the source BufferedImage to flip
     * @return a new BufferedImage that is a horizontally flipped version of the original
     */
    public static BufferedImage flipImage(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    /**
     * Rotates the given image 90 degrees clockwise.
     *
     * @param image the source BufferedImage to rotate
     * @return a new BufferedImage that is rotated 90 degrees clockwise
     */
    public static BufferedImage rotateImage90Clockwise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());

        AffineTransform tx = new AffineTransform();
        tx.translate(height, 0);
        tx.rotate(Math.toRadians(90));

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, rotatedImage);
    }
}
