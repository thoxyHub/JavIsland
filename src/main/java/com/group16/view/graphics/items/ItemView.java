package com.group16.view.graphics.items;

import com.group16.model.items.resources.Resources;
import com.group16.view.graphics.PositionedDrawable;
import com.group16.model.Subject;
import com.group16.view.ui.InventoryView;
import com.group16.view.Observer;

import java.awt.*;

/**
 * Represents a general visual representation of an item in the inventory.
 * Draws the item's sprite and quantity.
 */
public class ItemView implements PositionedDrawable, Observer {
    // The visual representation (sprite image) of the item
    protected Image sprite;

    // The quantity of the item to display (e.g., stack size)
    protected int quantity;

    /**
     * Constructs an ItemView with the given sprite and quantity.
     *
     * @param sprite   the image to draw
     * @param quantity the number of items represented
     */
    public ItemView(Image sprite, int quantity) {
        this.sprite = sprite;
        this.quantity = quantity;
    }

    /**
     * Draws the item in the inventory at the specified position.
     * If the quantity is greater than 1, it shows a count in the corner.
     *
     * @param g2   the graphics context
     * @param posX the x-position to draw the item
     * @param posY the y-position to draw the item
     */
    @Override
    public void draw(Graphics2D g2, int posX, int posY) {
        int size = InventoryView.TILE_INVENTORY_SIZE;

        g2.drawImage(sprite, posX, posY, size, size, null);

        if (quantity > 1) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String text = String.valueOf(quantity);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);

            g2.drawString(text, posX + size - textWidth - 4, posY + size - 4);
        }
    }

    /**
     * Updates the quantity based on the associated Subject (only if it's a Resources item).
     *
     * @param s the subject that notifies this observer
     */
    @Override
    public void update(Subject s) {
        if (!(s instanceof Resources res)) return;

        this.quantity = res.getQuantity();
    }
}
