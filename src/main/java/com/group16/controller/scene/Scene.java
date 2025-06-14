package com.group16.controller.scene;

import com.group16.controller.Updatable;
import com.group16.view.graphics.Drawable;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * The Scene interface defines the structure of a viewable and interactive scene
 * in the game, such as title screen, game screen, or end screen.
 * It combines update, draw, and key event handling capabilities.
 */
public interface Scene extends Updatable, Drawable {

    /**
     * Returns the Swing panel associated with this scene.
     * Can be overridden to return a custom JPanel.
     *
     * @return a JPanel representing the scene's UI
     */
    default JPanel getPanel() {
        return new JPanel();
    }

    /**
     * Hook method called when entering the scene.
     * Can be overridden to add setup logic.
     */
    default void onEnter() {
    }

    /**
     * Hook method called when exiting the scene.
     * Can be overridden to add teardown logic.
     */
    default void onExit() {
    }

    /**
     * Called when a key is pressed while this scene is active.
     *
     * @param e the KeyEvent
     */
    default void onKeyPressed(KeyEvent e) {
    }

    /**
     * Called when a key is released while this scene is active.
     *
     * @param e the KeyEvent
     */
    default void onKeyReleased(KeyEvent e) {
    }

    /**
     * Called when a key is typed while this scene is active.
     *
     * @param e the KeyEvent
     */
    default void onKeyTyped(KeyEvent e) {
    }
}
