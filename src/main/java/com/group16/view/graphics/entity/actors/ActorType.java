package com.group16.view.graphics.entity.actors;

import com.group16.model.area.Orientation;
import com.group16.view.Sprite;

import java.util.EnumMap;
import java.util.Map;
/**
 * Represents the type of actor (Player, Mob) and contains their associated sprite sets
 * for different actions (idle, walking, attacking) in each orientation.
 */
public enum ActorType {
    Player(PlayerSprites.frontIdle,
            PlayerSprites.rightIdle,
            PlayerSprites.backIdle,
            PlayerSprites.leftIdle,

            PlayerSprites.frontWalk,
            PlayerSprites.rightWalk,
            PlayerSprites.backWalk,
            PlayerSprites.leftWalk,

            PlayerSprites.frontAttack,
            PlayerSprites.rightAttack,
            PlayerSprites.backAttack,
            PlayerSprites.leftAttack,

            PlayerSprites.WALK_FRAME,
            PlayerSprites.ATTACK_FRAME,
            PlayerSprites.PLAYER_ANIMATION_INTERVAL),

    Mob(MobSprites.frontIdle,
            MobSprites.rightIdle,
            MobSprites.backIdle,
            MobSprites.leftIdle,

            MobSprites.frontWalk,
            MobSprites.rightWalk,
            MobSprites.backWalk,
            MobSprites.leftWalk,

            null,
            null,
            null,
            null,

            MobSprites.MOB_FRAME_COUNT,
            MobSprites.MOB_NO_ATTACK_FRAME,
            MobSprites.MOBS_ANIMATION_INTERVAL);

    private final Map<Orientation, Sprite> idleSprites = new EnumMap<>(Orientation.class);
    private final Map<Orientation, Sprite[]> walkAnimations = new EnumMap<>(Orientation.class);
    private final Map<Orientation, Sprite[]> attackAnimations = new EnumMap<>(Orientation.class);

    private final float animationInterval;
    private final int walkFrame;
    private final int attackFrame;

    ActorType(
            Sprite frontIdle,
            Sprite rightIdle,
            Sprite backIdle,
            Sprite leftIdle,

            Sprite[] frontWalk,
            Sprite[] rightWalk,
            Sprite[] backWalk,
            Sprite[] leftWalk,

            Sprite[] frontAttack,
            Sprite[] rightAttack,
            Sprite[] backAttack,
            Sprite[] leftAttack,

            int WALK_FRAME,
            int ATTACK_FRAME,
            float ANIMATION_INTERVAL
    ) {
        idleSprites.put(Orientation.SOUTH, frontIdle);
        idleSprites.put(Orientation.EAST, rightIdle);
        idleSprites.put(Orientation.NORTH,  backIdle);
        idleSprites.put(Orientation.WEST,  leftIdle);

        walkAnimations.put(Orientation.SOUTH, frontWalk);
        walkAnimations.put(Orientation.EAST, rightWalk);
        walkAnimations.put(Orientation.NORTH,  backWalk);
        walkAnimations.put(Orientation.WEST,  leftWalk);

        attackAnimations.put(Orientation.SOUTH, frontAttack);
        attackAnimations.put(Orientation.EAST, rightAttack);
        attackAnimations.put(Orientation.NORTH,  backAttack);
        attackAnimations.put(Orientation.WEST,  leftAttack);

        animationInterval = ANIMATION_INTERVAL;
        walkFrame = WALK_FRAME;
        attackFrame = ATTACK_FRAME;
    }

    public Sprite getIdleSprite(Orientation orientation) {
        return idleSprites.get(orientation);
    }

    public Sprite[] getWalkAnimation(Orientation orientation) {
        return walkAnimations.get(orientation);
    }

    public Sprite[] getAttackAnimation(Orientation orientation) {
        return attackAnimations.get(orientation);
    }

    public float getAnimationInterval() {
        return animationInterval;
    }

    public int getWalkFrame() {
        return walkFrame;
    }

    public int getAttackFrame() {
        return attackFrame;
    }
}
