package com.group16.view.ui;

import com.group16.view.graphics.Drawable;
import com.group16.view.Observer;
import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.items.Inventory;
import com.group16.model.items.resources.Item;
import com.group16.controller.config.GameConfig;
import com.group16.model.Subject;
import com.group16.view.graphics.items.ItemView;
import com.group16.view.graphics.items.ItemViewFactory;
import com.group16.view.graphics.items.PocketView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InventoryView is responsible for rendering the player's inventory UI.
 * It supports two modes: open inventory grid and pocket-only view.
 * Observes Inventory for changes to its contents and open state.
 */
public class InventoryView implements Observer, Drawable {

    // Size of one item tile in the inventory grid
    public static final int TILE_INVENTORY_SIZE = (int) (GameConfig.TILE_SIZE * (16f / 14));

    // Background sprite for the inventory grid
    private static final Sprite TABLE = new Sprite(
            "/sprites/ModularInventorySprites.png",
            new RegionOfInterest(28, 0, 75, 75)
    );

    // Views of inventory items
    private final List<ItemView> itemViews = new ArrayList<>();

    // Views of pocket items (e.g. hotbar)
    private final PocketView[] pocketViews = new PocketView[Inventory.NB_OF_POCKETS];

    // Layout constants
    private final int COLUMNS = 4;
    private final int ROWS = 4;
    private final int BORDER = (int) (TILE_INVENTORY_SIZE / 14f * 5);
    private final int SPACING = (int) (TILE_INVENTORY_SIZE / 14f * 3);

    // Computed layout dimensions
    private final int INVENTORY_WIDTH;
    private final int INVENTORY_HEIGHT;
    private final int START_X;
    private final int START_Y;

    private boolean isOpen = false;

    /**
     * Constructs the inventory view, calculating its layout and initializing pocket views.
     */
    public InventoryView() {
        INVENTORY_WIDTH = COLUMNS * TILE_INVENTORY_SIZE + (COLUMNS - 1) * SPACING + 2 * BORDER;
        INVENTORY_HEIGHT = ROWS * TILE_INVENTORY_SIZE + (ROWS - 1) * SPACING + 2 * BORDER;

        START_X = (GameConfig.SCREEN_WIDTH - INVENTORY_WIDTH) / 2;
        START_Y = (GameConfig.SCREEN_HEIGHT - INVENTORY_HEIGHT) / 2;

        for (int i = 0; i < Inventory.NB_OF_POCKETS; i++) {
            pocketViews[i] = new PocketView(null, 1, i);
        }
    }

    /**
     * Renders the inventory.
     * If inventory is open, draws full grid. Otherwise, draws only pocket items.
     *
     * @param g2 the graphics context
     */
    @Override
    public void draw(Graphics2D g2) {
        if (!isOpen) {
            drawPockets(g2);
        } else {
            drawBackground(g2);
            drawItems(g2);
        }
    }

    /**
     * Draws the inventory background image.
     */
    private void drawBackground(Graphics2D g2) {
        g2.drawImage(TABLE.getImage(), START_X, START_Y, INVENTORY_WIDTH, INVENTORY_HEIGHT, null);
    }

    /**
     * Draws the individual items inside the inventory grid.
     */
    private void drawItems(Graphics2D g2) {
        for (int i = 0; i < itemViews.size(); i++) {
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            int x = START_X + BORDER + col * (TILE_INVENTORY_SIZE + SPACING);
            int y = START_Y + BORDER + row * (TILE_INVENTORY_SIZE + SPACING);

            itemViews.get(i).draw(g2, x, y);
        }
    }

    /**
     * Draws the two pocket slots (hotbar) at the bottom of the screen.
     */
    private void drawPockets(Graphics2D g2) {
        int pocketSize = PocketView.POCKET_SIZE;
        int spacing = 20;

        int startX = pocketSize / 2;
        int y = GameConfig.SCREEN_HEIGHT - 2 * pocketSize;

        for (int i = 0; i < Inventory.NB_OF_POCKETS; i++) {
            PocketView pocketView = pocketViews[i];
            if (pocketView != null) {
                int x = startX + i * (pocketSize + spacing);
                pocketView.draw(g2, x, y);
            }
        }
    }

    /**
     * Updates the inventory UI when notified of changes.
     * Refreshes all item views and pocket item assignments.
     *
     * @param s the subject being observed (expected to be Inventory)
     */
    @Override
    public void update(Subject s) {
        if (!(s instanceof Inventory inventory)) return;

        itemViews.clear();

        for (Item item : inventory.getContents()) {
            ItemView view = ItemViewFactory.createView(item);
            item.registerObserver(view);
            itemViews.add(view);
        }

        isOpen = inventory.isOpen();

        for (int i = 0; i < Inventory.NB_OF_POCKETS; i++) {
            Item item = (Item) inventory.getPocketItem(i);
            ItemView itemView = (item != null) ? ItemViewFactory.createView(item) : null;
            int quantity = (item != null) ? item.getQuantity() : 0;

            pocketViews[i] = new PocketView(itemView, quantity, i);
        }
    }
}
