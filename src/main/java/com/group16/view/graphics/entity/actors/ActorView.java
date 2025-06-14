package com.group16.view.graphics.entity.actors;

import com.group16.model.utils.maths.Vector;
import com.group16.model.area.Orientation;
import com.group16.model.entity.actors.Actors;
import com.group16.model.entity.actors.State;
import com.group16.controller.Updatable;
import com.group16.model.Subject;
import com.group16.view.Sprite;
import com.group16.view.graphics.entity.EntityView;

import java.awt.*;

/**
 * Responsible for rendering animated actors (Player or Mob) with different states and orientations.
 * Tracks the current state and orientation to display the correct animation frame.
 */
public final class ActorView extends EntityView implements Updatable {

    private Orientation orientation;
    private State state;

    // Animation control
    private int animFrame = 0;           // Current frame index
    private float animTimer = 0;         // Accumulated time for animation

    private final ActorType actorType;

    /**
     * Creates an ActorView for a given ActorType at a starting position.
     *
     * @param actorType the type of actor (Player or Mob)
     * @param initialPos the initial position in world coordinates
     */
    public ActorView(ActorType actorType, Vector initialPos) {
        super(actorType.getIdleSprite(Orientation.SOUTH), initialPos);
        this.actorType = actorType;
    }

    /**
     * Draws the actor using its current sprite frame, offset by camera.
     */
    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        super.draw(g2, cameraX, cameraY);
    }

    /**
     * Updates the orientation and state based on the actor's current state.
     *
     * @param s the subject being observed (should be an instance of Actors)
     */
    @Override
    public void update(Subject s) {
        super.update(s);
        assert s instanceof Actors;
        Actors a = (Actors) s;

        // Update orientation and state
        orientation = a.getCurrentOrientation();
        state = a.getState();
    }

    /**
     * Called each frame to advance the animation depending on the actor's state.
     */
    @Override
    public void update(float deltaTime) {
        updateAnimation(deltaTime);

        if (state != null) {
            switch (state) {
                case WALK -> walkFrame(animFrame);
                case SLASH -> slashFrame(animFrame);
                default -> idleFrame();
            }
        }
    }

    /**
     * Advances the animation timer and frame index if enough time has passed.
     */
    private void updateAnimation(float deltaTime) {
        int frameCount;

        if (state == null) {
            animFrame = 0;
            animTimer = 0;
            return;
        }

        switch (state) {
            case WALK -> frameCount = actorType.getWalkFrame();
            case SLASH -> frameCount = actorType.getAttackFrame();
            default -> {
                animFrame = 0;
                animTimer = 0;
                return;
            }
        }

        animTimer += deltaTime;
        if (animTimer >= actorType.getAnimationInterval()) {
            animTimer -= actorType.getAnimationInterval();
            animFrame = (animFrame + 1) % frameCount;
        }
    }

    /**
     * Sets the sprite to the idle frame for the current orientation.
     */
    private void idleFrame() {
        sprite = actorType.getIdleSprite(orientation);
    }

    /**
     * Sets the sprite to the appropriate walking animation frame.
     */
    private void walkFrame(int animFrame) {
        Sprite[] walk = actorType.getWalkAnimation(orientation);
        sprite = safeGetFrame(walk, animFrame);
    }

    /**
     * Sets the sprite to the appropriate attack animation frame.
     */
    private void slashFrame(int animFrame) {
        Sprite[] attack = actorType.getAttackAnimation(orientation);
        sprite = safeGetFrame(attack, animFrame);
    }

    /**
     * Safely retrieves the animation frame or falls back to the default idle sprite.
     */
    private Sprite safeGetFrame(Sprite[] sprites, int index) {
        if (sprites == null || index < 0 || index >= sprites.length) {
            return actorType.getIdleSprite(Orientation.SOUTH); // default fallback
        }
        return sprites[index];
    }
}
