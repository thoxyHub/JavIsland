package com.group16.view.graphics.items;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.ui.InventoryView;

import java.awt.*;

/**
 * Represents the graphical view of a gun item in the inventory.
 * Handles how the gun is drawn inside the inventory grid with scaled dimensions.
 */
public class GunView extends ItemView {

    // Path and region of interest for the gun sprite
    private static final String IMG_PATH = "/sprites/pistol.png";
    private static final RegionOfInterest ROI = new RegionOfInterest(0, 0, 125, 83);
    private static final Sprite SPRITE = new Sprite(IMG_PATH, ROI);

    // Scaling factors for display in inventory
    private static final float X_SCALE = 0.8f; // 80% of inventory tile width
    private static final float Y_SCALE = 0.6f; // 60% of inventory tile height

    /**
     * Constructs a GunView with the specified quantity.
     *
     * @param quantity the number of guns (should typically be 1)
     */
    public GunView(int quantity) {
        super(SPRITE.getImage(), quantity);
    }

    /**
     * Draws the gun sprite within the inventory tile with scaling and optional quantity label.
     *
     * @param g2    the graphics context
     * @param posX  the x-coordinate in the panel
     * @param posY  the y-coordinate in the panel
     */
    @Override
    public void draw(Graphics2D g2, int posX, int posY) {
        int xScaledSize = (int) (InventoryView.TILE_INVENTORY_SIZE * X_SCALE);
        int yScaledSize = (int) (InventoryView.TILE_INVENTORY_SIZE * Y_SCALE);
        int offsetX = posX + (InventoryView.TILE_INVENTORY_SIZE - xScaledSize) / 2;
        int offsetY = posY + (InventoryView.TILE_INVENTORY_SIZE - yScaledSize) / 2;

        g2.drawImage(sprite, offsetX, offsetY, xScaledSize, yScaledSize, null);

        // If there are multiple instances, draw quantity
        if (quantity > 1) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String text = String.valueOf(quantity);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, offsetX + xScaledSize - textWidth - 4, offsetY + yScaledSize - 4);
        }
    }
}
