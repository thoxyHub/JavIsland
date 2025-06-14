package com.group16.view.graphics.entity.actors;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.utils.ImageManipulation;

/**
 * Holds all sprite definitions for the player character,
 * including idle, walking, and attack animations in all four directions.
 */
public final class PlayerSprites {

    private static final String IMAGE_ADDRESS = "/sprites/Player.png";

    // Animation timing and frame configuration
    public static final float PLAYER_ANIMATION_INTERVAL = 120f; // Duration between animation frames in milliseconds
    public static final int WALK_FRAME = 6;                     // Number of frames in walk animation
    public static final int ATTACK_FRAME = 4;                   // Number of frames in attack animation

    // Idle sprites for each direction
    public static final Sprite frontIdle;
    public static final Sprite rightIdle;
    public static final Sprite backIdle;
    public static final Sprite leftIdle;

    // Arrays for walking animations in each direction
    public static final Sprite[] frontWalk = new Sprite[WALK_FRAME];
    public static final Sprite[] rightWalk = new Sprite[WALK_FRAME];
    public static final Sprite[] backWalk  = new Sprite[WALK_FRAME];
    public static final Sprite[] leftWalk  = new Sprite[WALK_FRAME];

    // Arrays for attacking animations in each direction
    public static final Sprite[] frontAttack = new Sprite[WALK_FRAME];
    public static final Sprite[] rightAttack = new Sprite[WALK_FRAME];
    public static final Sprite[] backAttack  = new Sprite[WALK_FRAME];
    public static final Sprite[] leftAttack  = new Sprite[WALK_FRAME];

    // Static block to initialize all sprites
    static {
        // Idle sprites (32x32 px)
        frontIdle = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 0, 32, 32));
        rightIdle = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 32, 32, 32));
        backIdle  = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 64, 32, 32));
        leftIdle  = new Sprite(ImageManipulation.flipImage(rightIdle.getImage()));

        // Walk and attack animations, frame-by-frame across rows
        for (int i = 0; i < WALK_FRAME; i++) {
            frontWalk[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 96, 32, 32));
            rightWalk[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 128, 32, 32));
            backWalk[i]  = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 160, 32, 32));
            leftWalk[i]  = new Sprite(ImageManipulation.flipImage(rightWalk[i].getImage()));

            frontAttack[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 192, 32, 32));
            rightAttack[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 224, 32, 32));
            backAttack[i]  = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 256, 32, 32));
            leftAttack[i]  = new Sprite(ImageManipulation.flipImage(rightAttack[i].getImage()));
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * This utility class is intended for static access only.
     */
    private PlayerSprites() {}
}
