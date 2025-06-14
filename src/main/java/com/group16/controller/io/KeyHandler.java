package com.group16.controller.io;

import com.group16.controller.SceneManager;

import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Handles keyboard input by implementing the {@link KeyListener} interface.
 * Tracks which keys are currently pressed and delegates input events to the active scene via {@link SceneManager}.
 */
public class KeyHandler implements KeyListener {

    /** Stores the state (pressed or not) of each key code */
    private static HashMap<Integer, Boolean> keys;

    /** Reference to the SceneManager to forward input events to the current active scene */
    private final SceneManager sceneManager;

    /**
     * Constructs a KeyHandler that communicates with a SceneManager.
     *
     * @param sceneManager the manager responsible for switching and handling scenes
     */
    public KeyHandler(SceneManager sceneManager) {
        keys = new HashMap<>();
        this.sceneManager = sceneManager;
    }

    /**
     * Checks whether a given key code is currently pressed.
     *
     * @param code the key code to check (from {@link java.awt.event.KeyEvent})
     * @return true if the key is currently pressed, false otherwise
     */
    public boolean getKeyValue(int code) {
        return keys.getOrDefault(code, false);
    }

    /**
     * Called when a key is typed (pressed and released quickly).
     * Delegates to the current scene.
     *
     * @param e the key event
     */
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        sceneManager.getCurrent().onKeyTyped(e);
    }

    /**
     * Called when a key is pressed.
     * Updates the internal key state and notifies the current scene.
     *
     * @param e the key event
     */
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        keys.put(e.getKeyCode(), true);
        sceneManager.getCurrent().onKeyPressed(e);
    }

    /**
     * Called when a key is released.
     * Updates the internal key state and notifies the current scene.
     *
     * @param e the key event
     */
    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        keys.put(e.getKeyCode(), false);
        sceneManager.getCurrent().onKeyReleased(e);
    }
}
