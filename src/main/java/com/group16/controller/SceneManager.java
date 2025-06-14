package com.group16.controller;

import com.group16.controller.scene.Scene;

import java.util.HashMap;
import java.util.Map;

/**
 * SceneManager is responsible for managing different game scenes.
 * It allows switching between scenes and maintains the current active scene.
 */
public final class SceneManager {

    // Stores all available scenes by name
    private final Map<String, Scene> scenes = new HashMap<>();

    // The currently active scene
    private Scene current;

    /**
     * Adds a new scene to the manager.
     *
     * @param name  the identifier for the scene
     * @param scene the Scene object to associate with the name
     */
    public void add(String name, Scene scene) {
        scenes.put(name, scene);
    }

    /**
     * Switches the currently active scene to the one associated with the given name.
     * Calls `onExit` on the old scene and `onEnter` on the new scene.
     *
     * @param name the name of the scene to switch to
     * @throws IllegalArgumentException if the named scene does not exist
     */
    public void switchTo(String name) {
        if (current != null) current.onExit();

        current = scenes.get(name);

        if (current == null)
            throw new IllegalArgumentException("Scene \"" + name + "\" does not exist");

        current.onEnter();
    }

    /**
     * Returns the currently active scene.
     *
     * @return the current Scene object
     */
    public Scene getCurrent() {
        return current;
    }
}
