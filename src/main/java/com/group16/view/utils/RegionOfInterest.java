package com.group16.view.utils;

/**
 * Represents a rectangular region of interest (ROI) in an image or sprite sheet.
 *
 * @param x1     the x-coordinate of the top-left corner
 * @param y1     the y-coordinate of the top-left corner
 * @param width  the width of the region
 * @param height the height of the region
 */
public record RegionOfInterest(int x1, int y1, int width, int height) {

    /**
     * @return the x-coordinate of the bottom-right corner (exclusive)
     */
    public int x2() {
        return x1 + width;
    }

    /**
     * @return the y-coordinate of the bottom-right corner (exclusive)
     */
    public int y2() {
        return y1 + height;
    }
}
