package com.group16.controller.scene;

import com.group16.view.graphics.Drawable;
import com.group16.view.ui.HealthBarView;
import com.group16.view.ui.InventoryView;
import com.group16.model.area.Island;
import com.group16.model.area.IslandMapGenerator;
import com.group16.model.entity.actors.Player;
import com.group16.model.area.tiles.Tile;
import com.group16.controller.GameLogic;
import com.group16.view.ui.GamePanel;
import com.group16.controller.SceneManager;
import com.group16.controller.config.GameConfig;
import com.group16.model.utils.maths.Vector;
import com.group16.view.graphics.area.Camera;
import com.group16.view.graphics.area.TileViewFactory;
import com.group16.view.graphics.area.WorldView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameScene handles the active game state, coordinating models (Island, Player), views, and input handling.
 * It integrates the game logic, manages transitions between scenes, and is responsible for resetting and redrawing the game environment.
 */
public final class GameScene implements Scene {

    /** Manages scene transitions */
    private final SceneManager sceneManager;

    /** Main game panel for rendering the game UI */
    private GamePanel gamePanel;

    /** Handles game logic and input */
    private GameLogic gameLogic;

    // MODELS
    /** The player character */
    private Player player;

    /** The island map/environment */
    private Island island;

    /** Generates the island map from a text file */
    private IslandMapGenerator mapGenerator;

    // VIEWS
    /** List of drawable UI and game components */
    private final List<Drawable> views = new ArrayList<>();

    /** Camera that tracks the player */
    private Camera camera;

    /** Starting pixel coordinates for the player */
    private int startX;
    private int startY;

    /** Map selection index */
    private int mapSelection = 1;

    /** Number of available maps */
    private static final int NUMBER_OF_MAP = 2;

    /**
     * Constructor for GameScene.
     *
     * @param sm the scene manager for controlling scene transitions
     * @throws IOException if loading the map fails
     */
    public GameScene(SceneManager sm) throws IOException {
        this.sceneManager = sm;
        resetGame(mapSelection);
    }

    /**
     * Updates the game state and logic each frame.
     * If the game is over, resets and transitions to the end scene.
     *
     * @param dt time delta in nanoseconds
     */
    @Override
    public void update(float dt) {
        if (gameLogic.isGameOver()) {
            try {
                resetGame(mapSelection);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sceneManager.switchTo("end");
        } else {
            gameLogic.update(dt);
            gamePanel.update(dt);
        }
    }

    /**
     * Returns the panel associated with the game scene for rendering.
     *
     * @return the game panel
     */
    public JPanel getPanel() {
        return gamePanel;
    }

    /**
     * Draws the contents of the game scene onto the graphics context.
     *
     * @param g2 the graphics context
     */
    @Override
    public void draw(Graphics2D g2) {
        gamePanel.draw(g2);
    }

    /* ================== Input Handling ================== */

    /**
     * Handles key press events. ESC switches to the title screen.
     *
     * @param e key event
     */
    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            sceneManager.switchTo("title");
        }
        gameLogic.onKeyPressed(e);
    }

    /**
     * Forwards key release events to game logic.
     *
     * @param e key event
     */
    @Override
    public void onKeyReleased(KeyEvent e) {
        gameLogic.onKeyReleased(e);
    }

    /**
     * Forwards key typed events to game logic.
     *
     * @param e key event
     */
    @Override
    public void onKeyTyped(KeyEvent e) {
        gameLogic.onKeyTyped(e);
    }

    /* ================== Model Accessors ================== */

    /**
     * Gets the current island instance.
     *
     * @return the island
     */
    public Island getIsland() {
        return island;
    }

    /**
     * Gets the player entity.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /* ================== Game Reset / Initialization ================== */

    /**
     * Resets the game to a fresh state based on the selected map.
     *
     * @param mapSelection map index to load
     * @throws IOException if the map file cannot be read
     */
    public void resetGame(int mapSelection) throws IOException {
        views.clear();

        mapGenerator = new IslandMapGenerator("src/main/resources/MapDesign" + mapSelection + ".txt");
        startX = mapGenerator.getStartX();
        startY = mapGenerator.getStartY();

        resetModels();

        // Reset the logic
        gameLogic = new GameLogic(island, player);

        // Reset the views
        gamePanel = new GamePanel(views);
        gameLogic.registerObserver(gamePanel);
    }

    /**
     * Resets game models (island, player, views) to initial state.
     *
     * @throws IOException if model generation fails
     */
    public void resetModels() throws IOException {
        Tile[][] map = mapGenerator.getMap();

        island = new Island(map);

        player = new Player(island, new Vector((float) startX / GameConfig.TILE_SIZE, (float) startY / GameConfig.TILE_SIZE));
        camera = new Camera(player); // To make sure that the player is always centered

        WorldView worldView = new WorldView(camera, TileViewFactory.createTileViewMap(map, IslandMapGenerator.MAP_BORDER));
        island.registerObserver(worldView);

        // Will create his view and register it as the observer
        island.addPlayer(player);

        InventoryView inventoryView = new InventoryView();
        player.getInventory().registerObserver(inventoryView);

        HealthBarView healthBarView = new HealthBarView(Player.PLAYER_MAX_HEALTH);
        player.registerObserver(healthBarView);

        // The order here defines the order in which it will be drawn
        views.add(worldView);
        views.add(healthBarView);
        views.add(inventoryView);
    }

    /* ================== Map Selection ================== */

    /**
     * Changes the selected map and resets the game accordingly.
     *
     * @param newMap the index of the new map to load
     */
    public void setMapSelection(int newMap) {
        assert (newMap > 0 && newMap <= NUMBER_OF_MAP);
        mapSelection = newMap;
        try {
            resetGame(newMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
