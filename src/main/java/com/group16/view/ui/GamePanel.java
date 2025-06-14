package com.group16.view.ui;

import com.group16.controller.GameLogic;
import com.group16.controller.Updatable;
import com.group16.controller.config.GameConfig;
import com.group16.model.Subject;
import com.group16.view.Observer;
import com.group16.view.graphics.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GamePanel is responsible for rendering the main game interface including
 * all Drawable views and overlay UI like wave numbers and preparation timers.
 */
public class GamePanel extends JPanel implements Observer, Updatable {

    private List<Drawable> views;

    private int waveNumber;
    private float waveStartDisplayTime;
    private float preparationTime;

    private boolean isPreparing;

    // Constants for UI layout and sizing
    private final int ICON_WAVE_SIZE = 50;
    private final int BOX_TIMER_WIDTH = 100;
    private final int BOX_TIMER_HEIGHT = 50;
    private final int BORDER_WIDTH_SIZE = 20;
    private final int BORDER_HEIGHT_SIZE = 50;

    /**
     * Constructs a GamePanel to render the game UI and registered Drawable views.
     *
     * @param views A list of views to be rendered in draw order.
     */
    public GamePanel(List<Drawable> views) {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.views = views;
    }

    /**
     * Externally triggers rendering using a provided Graphics2D object.
     *
     * @param g2 The graphics context to draw with.
     */
    public void draw(Graphics2D g2) {
        paintComponent(g2);
    }

    /**
     * Core rendering method called by the Swing framework.
     *
     * @param g The graphics context.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw the views in order
        for (Drawable view : views) {
            view.draw(g2);
        }

        if (isPreparing) {
            drawTimerBox(g2);
        }

        // Either show level title or persistent wave number
        if (waveStartDisplayTime > 0) {
            drawLevelIndicator(g2);
        } else {
            drawWaveNumber(g2);
        }
    }

    /**
     * Draws a semi-transparent banner in the center with the current wave number.
     *
     * @param g2 The graphics context.
     */
    private void drawLevelIndicator(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, GameConfig.SCREEN_HEIGHT / 2 - 100, GameConfig.SCREEN_WIDTH, 100);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Verdana", Font.BOLD, 40));
        String levelText = "Level " + waveNumber;
        int levelTextWidth = g2.getFontMetrics().stringWidth(levelText);
        g2.drawString(levelText, (GameConfig.SCREEN_WIDTH - levelTextWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 - 35);
    }

    /**
     * Draws a circular icon with the wave number at the bottom right.
     *
     * @param g2 The graphics context.
     */
    private void drawWaveNumber(Graphics2D g2) {
        int iconX = GameConfig.SCREEN_WIDTH - ICON_WAVE_SIZE - BORDER_WIDTH_SIZE;
        int iconY = GameConfig.SCREEN_HEIGHT - ICON_WAVE_SIZE - BORDER_HEIGHT_SIZE;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillOval(iconX, iconY, ICON_WAVE_SIZE, ICON_WAVE_SIZE);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Verdana", Font.BOLD, 20));
        String waveText = String.valueOf(waveNumber);
        int waveTextWidth = g2.getFontMetrics().stringWidth(waveText);
        int waveTextX = iconX + (ICON_WAVE_SIZE - waveTextWidth) / 2;
        int waveTextY = iconY + (ICON_WAVE_SIZE + g2.getFontMetrics().getAscent()) / 2 - 5;
        g2.drawString(waveText, waveTextX, waveTextY);
    }

    /**
     * Draws the timer box indicating preparation countdown before a wave starts.
     *
     * @param g2 The graphics context.
     */
    private void drawTimerBox(Graphics2D g2) {
        int boxX = GameConfig.SCREEN_WIDTH - BOX_TIMER_WIDTH - ICON_WAVE_SIZE - 2 * BORDER_WIDTH_SIZE;
        int boxY = GameConfig.SCREEN_HEIGHT - BOX_TIMER_HEIGHT - BORDER_HEIGHT_SIZE;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(boxX, boxY, BOX_TIMER_WIDTH, BOX_TIMER_HEIGHT, 15, 15);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Verdana", Font.BOLD, 20));

        int minutes = (int) preparationTime / 60;
        int seconds = (int) preparationTime % 60;
        String timerText = String.format("%02d:%02d", minutes, seconds);

        int textWidth = g2.getFontMetrics().stringWidth(timerText);
        int textX = boxX + (BOX_TIMER_WIDTH - textWidth) / 2;
        int textY = boxY + (BOX_TIMER_HEIGHT + g2.getFontMetrics().getAscent()) / 2 - 5;

        g2.drawString(timerText, textX, textY);
    }

    /**
     * Updates the internal game state variables from the observed GameLogic subject.
     *
     * @param s The observed subject.
     */
    @Override
    public void update(Subject s) {
        assert s instanceof GameLogic;

        GameLogic gameLogic = (GameLogic) s;

        waveNumber = gameLogic.getWaveNumber();
        waveStartDisplayTime = gameLogic.getWaveStartDisplayTime();
        preparationTime = gameLogic.getPreparationTime();
        isPreparing = gameLogic.getIsPreparing();
    }

    /**
     * Updates all drawable views that are also Updatable each frame.
     *
     * @param deltaTime The time since the last update (in nanoseconds).
     */
    @Override
    public void update(float deltaTime) {
        views.forEach(v -> {
            if (v instanceof Updatable) ((Updatable) v).update(deltaTime);
        });
    }
}
