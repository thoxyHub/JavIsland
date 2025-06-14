package com.group16.controller.scene;

import com.group16.view.ui.EndPanel;
import com.group16.controller.SceneManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Represents the end scene of the game, typically displayed after the player loses or finishes the game.
 * Provides keyboard input handling to allow restarting the game or returning to the title screen.
 */
public class EndScene implements Scene {

    /** Reference to the scene manager for transitioning between scenes */
    private final SceneManager sceneManager;

    /** The panel associated with this scene (UI view) */
    private final EndPanel panel = new EndPanel();

    /**
     * Constructor for EndScene.
     *
     * @param sceneManager the manager that handles switching between scenes
     */
    public EndScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /*
        private void restartGame() throws IOException {
        isGameOver = false;

        this.mapWidth = RandomIslandMapGenerator.WIDTH;
        this.mapHeight = RandomIslandMapGenerator.HEIGHT;

        gameController.restart();

        waveNumber = 1;
    }
     */

    /**
     * Returns the panel associated with this scene.
     *
     * @return the JPanel that represents this scene's visual component
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Called on each game loop iteration to update the scene.
     * This scene does not require periodic updates.
     *
     * @param dt the time elapsed since the last update, in nanoseconds
     */
    @Override
    public void update(float dt) {
        // No logic needed for static end screen
    }

    /**
     * Handles custom drawing for the scene.
     * Drawing is handled by the Swing panel itself, so this is left empty.
     *
     * @param g2 the graphics context to draw with
     */
    @Override
    public void draw(Graphics2D g2) {
        // Drawing is handled by the Swing EndPanel
    }

    /**
     * Handles key press events while in the end scene.
     * - Pressing 'R' restarts the game.
     * - Pressing 'ESC' returns to the title screen.
     *
     * @param e the key event
     */
    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            sceneManager.switchTo("game");
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            sceneManager.switchTo("title");
        }
    }

    /**
     * Handles key release events.
     * This method is currently unused in the end scene.
     *
     * @param e the key event
     */
    @Override
    public void onKeyReleased(KeyEvent e) {
    }

    /**
     * Handles key typed events.
     * This method is currently unused in the end scene.
     *
     * @param e the key event
     */
    @Override
    public void onKeyTyped(KeyEvent e) {
    }
}
