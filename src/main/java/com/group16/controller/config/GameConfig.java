package com.group16.controller.config;

import com.group16.model.utils.maths.Vector;

/**
 * A utility class that stores global configuration constants for the game,
 * including screen size, tile scaling, frame rate, and rendering parameters.
 *
 * This class is not meant to be instantiated.
 */
public final class GameConfig {

    // Private constructor to prevent instantiation
    private GameConfig() {}

    /** Original size of a tile in pixels before scaling */
    private static final int ORIGINAL_TILE_SIZE = 16;

    /** Scale factor applied to the original tile size for rendering */
    public static final int SCALE = 3;

    /** Final tile size after scaling */
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    /** Number of horizontal tiles on the screen */
    public static final int NUM_TILE_COL = 17;

    /** Number of vertical tiles on the screen */
    public static final int NUM_TILE_ROW = 13;

    /** Total screen width in pixels */
    public static final int SCREEN_WIDTH = TILE_SIZE * NUM_TILE_COL;

    /** Total screen height in pixels */
    public static final int SCREEN_HEIGHT = TILE_SIZE * NUM_TILE_ROW;

    /** The center point of the screen, used for rendering camera focus */
    public static final Vector PANEL_CENTER = new Vector(
            (float) (SCREEN_WIDTH / 2 - TILE_SIZE / 2),
            (float) (SCREEN_HEIGHT / 2 - TILE_SIZE / 2)
    );

    /** Target frames per second for the game loop */
    public static final int FPS = 20;

    /** Constant representing one second in nanoseconds */
    public static final float ONE_SECOND_NS = 1_000_000_000f;

    /** Duration of a single frame based on target FPS */
    public static final float FRAME_DURATION = ONE_SECOND_NS / FPS;
}
