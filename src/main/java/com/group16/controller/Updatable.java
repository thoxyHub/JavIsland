package com.group16.controller;

/**
 * Represents an object that can be updated every frame with the elapsed time.
 * Typically used in the game loop to update state (e.g., animations, movement, timers).
 */
public interface Updatable {

    /**
     * Updates the object based on the elapsed time since the last frame.
     *
     * @param deltaTime time passed in nanoseconds since the last update call
     */
    void update(float deltaTime);
}
