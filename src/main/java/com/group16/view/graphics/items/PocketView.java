package com.group16.view.graphics.items;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.ui.InventoryView;

import java.awt.*;

/**
 * A specialized {@link ItemView} used for rendering equipped items (pockets) in the inventory UI.
 * Displays a larger background frame and includes an index label.
 */
public class PocketView extends ItemView {

    // Constants defining the region of the background sprite
    private static final int IMAGE_POCKET_X = 0;
    private static final int IMAGE_POCKET_Y = 0;
    private static final int IMAGE_POCKET_WIDTH = 32;
    private static final int IMAGE_POCKET_HEIGHT = 32;

    // Background sprite for pocket slots
    private static final Sprite POCKET_BG = new Sprite(
            "/sprites/gearDisplay.png",
            new RegionOfInterest(IMAGE_POCKET_X, IMAGE_POCKET_Y, IMAGE_POCKET_WIDTH, IMAGE_POCKET_HEIGHT)
    );

    // Size scaling for pocket visuals
    private static final float POCKET_SCALE = 1.3f;
    public static final int POCKET_SIZE = (int) (InventoryView.TILE_INVENTORY_SIZE * POCKET_SCALE);

    private final int index;         // Pocket index (0-based)
    private final ItemView itemView; // View of the item in the pocket

    /**
     * Constructs a PocketView for a specific item and pocket index.
     *
     * @param itemView the view of the item contained in the pocket
     * @param quantity the item quantity
     * @param index    the pocket index (e.g., 0 for Sword, 1 for Gun)
     */
    public PocketView(ItemView itemView, int quantity, int index) {
        super(POCKET_BG.getImage(), quantity);
        this.index = index;
        this.itemView = itemView;
    }

    /**
     * Renders the pocket background, the item inside it, and an index label.
     *
     * @param g2    the graphics context to draw on
     * @param posX  X coordinate on screen
     * @param posY  Y coordinate on screen
     */
    @Override
    public void draw(Graphics2D g2, int posX, int posY) {
        // Draw background frame
        g2.drawImage(sprite, posX, posY, POCKET_SIZE, POCKET_SIZE, null);

        // Draw the item slightly shifted to the bottom-right
        if (itemView != null) {
            int offsetX = posX + (int) (POCKET_SIZE * 0.15);
            int offsetY = posY + (int) (POCKET_SIZE * 0.15);
            itemView.draw(g2, offsetX, offsetY);
        }

        // Draw pocket index label ("S" for sword, "G" for gun)
        String indexLabel = switch (index) {
            case 0 -> "S";
            case 1 -> "G";
            default -> String.valueOf(index + 1);
        };

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(indexLabel, posX + POCKET_SIZE - 12, posY + POCKET_SIZE - 6);
    }
}
