package com.group16.view.graphics.entity.actors;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.view.utils.ImageManipulation;

/**
 * Loads and stores sprites for mob characters (e.g., skeletons).
 * Provides directional idle and walking animations for mobs.
 */
public final class MobSprites {

    private static final String IMAGE_ADDRESS = "/sprites/Skeleton.png";

    // Animation configuration constants
    public static final float MOBS_ANIMATION_INTERVAL = 20; // Duration between animation frames
    public static final int MOB_FRAME_COUNT = 6;            // Number of frames in a walk animation
    public static final int MOB_NO_ATTACK_FRAME = 0;        // No attack animation for mobs

    // Idle sprites for each direction
    public static final Sprite frontIdle;
    public static final Sprite backIdle;
    public static final Sprite rightIdle;
    public static final Sprite leftIdle;

    // Arrays for walking animations in each direction
    public static final Sprite[] frontWalk = new Sprite[MOB_FRAME_COUNT];
    public static final Sprite[] backWalk  = new Sprite[MOB_FRAME_COUNT];
    public static final Sprite[] rightWalk = new Sprite[MOB_FRAME_COUNT];
    public static final Sprite[] leftWalk  = new Sprite[MOB_FRAME_COUNT];

    // Static initializer to load all sprites once at class load
    static {
        // Idle sprites for each direction (64x64 regions)
        frontIdle = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 0, 32, 32));
        backIdle  = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 64, 32, 32));
        rightIdle = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(0, 192, 32, 32));
        leftIdle  = new Sprite(ImageManipulation.flipImage(rightIdle.getImage()));

        // Walking animations: extract frames horizontally
        for (int i = 0; i < MOB_FRAME_COUNT; i++) {
            backWalk[i]  = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32,   64, 32, 32));
            frontWalk[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 0, 32, 32));
            rightWalk[i] = new Sprite(IMAGE_ADDRESS, new RegionOfInterest(i * 32, 32, 32, 32));
            leftWalk[i]  = new Sprite(ImageManipulation.flipImage(rightWalk[i].getImage()));
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used statically.
     */
    private MobSprites() {}
}
