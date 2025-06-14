package com.group16.view.ui;

import com.group16.model.entity.actors.Player;
import com.group16.controller.config.GameConfig;
import com.group16.view.graphics.Drawable;
import com.group16.view.Observer;
import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.Subject;

import java.awt.*;

/**
 * HealthBarView displays the player's current health visually as a sprite-based health bar.
 * It observes the player and updates whenever the player's health changes.
 */
public class HealthBarView implements Observer, Drawable {

    // Number of health states (sprites)
    private final int NB_OF_STATES = 7;

    // Size of each health bar sprite in the source image
    private final int WIDTH = 33;
    private final int HEIGHT = 11;

    private final int maxHealth;
    private final int hpPerState;
    private final Sprite[] healthSprites;
    private int currentHealth;

    /**
     * Constructs a health bar view with the given max health.
     * Preloads all sprites corresponding to different health states.
     *
     * @param maxHealth The maximum health the player can have.
     */
    public HealthBarView(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.hpPerState = maxHealth / (NB_OF_STATES - 1);  // Divide health into discrete sprite states

        healthSprites = new Sprite[NB_OF_STATES];
        for (int i = 0; i < NB_OF_STATES; i++) {
            healthSprites[i] = new Sprite(
                    "/sprites/HealthUI.png",
                    new RegionOfInterest(0, i * HEIGHT, WIDTH, HEIGHT)
            );
        }
    }

    /**
     * Draws the health bar sprite based on the current health value.
     *
     * @param g2 The graphics context to draw on.
     */
    @Override
    public void draw(Graphics2D g2) {
        int state = (maxHealth - currentHealth) / hpPerState;

        // Clamp the state value within sprite array bounds
        if (state < 0) {
            state = 0;
        } else if (state >= healthSprites.length) {
            state = healthSprites.length - 1;
        }

        g2.drawImage(
                healthSprites[state].getImage(),
                10, 10,                             // Top-left corner of health bar
                GameConfig.TILE_SIZE * 3,          // Scaled width
                GameConfig.TILE_SIZE,              // Scaled height
                null
        );
    }

    /**
     * Updates the current health when the player changes.
     *
     * @param s The subject being observed (expected to be a Player).
     */
    @Override
    public void update(Subject s) {
        assert (s instanceof Player);
        Player player = (Player) s;
        currentHealth = player.getHealth();
    }
}
