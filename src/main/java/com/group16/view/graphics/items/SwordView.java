package com.group16.view.graphics.items;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.ui.InventoryView;

import java.awt.*;

/**
 * {@code SwordView} is the graphical representation of the sword item
 * displayed inside the player's inventory.
 */
public class SwordView extends ItemView {

    // Path to the sprite image representing the sword
    private static final String IMG_PATH = "/sprites/sword.png";

    // Region of interest in the sprite (assumes 16x16 area)
    private static final RegionOfInterest ROI = new RegionOfInterest(0, 0, 16, 16);

    // Preloaded sprite instance
    private static final Sprite SPRITE = new Sprite(IMG_PATH, ROI);

    // Scale factor for rendering the sword icon smaller than the full inventory tile size
    private static final float SCALE = 0.8f;

    /**
     * Constructs a new {@code SwordView} with the given quantity.
     *
     * @param quantity the number of swords shown in the inventory (typically 1)
     */
    public SwordView(int quantity) {
        super(SPRITE.getImage(), quantity);
    }

    /**
     * Draws the sword item in a scaled-down format, centered in the inventory tile.
     * Also overlays the quantity if more than one.
     *
     * @param g2    the graphics context
     * @param posX  the x-position in pixels
     * @param posY  the y-position in pixels
     */
    @Override
    public void draw(Graphics2D g2, int posX, int posY) {
        int scaledSize = (int) (InventoryView.TILE_INVENTORY_SIZE * SCALE);
        int offsetX = posX + (InventoryView.TILE_INVENTORY_SIZE - scaledSize) / 2;
        int offsetY = posY + (InventoryView.TILE_INVENTORY_SIZE - scaledSize) / 2;

        g2.drawImage(sprite, offsetX, offsetY, scaledSize, scaledSize, null);

        if (quantity > 1) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String text = String.valueOf(quantity);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, offsetX + scaledSize - textWidth - 4, offsetY + scaledSize - 4);
        }
    }
}
