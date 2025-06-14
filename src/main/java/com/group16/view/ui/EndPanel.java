package com.group16.view.ui;

import com.group16.controller.Updatable;
import com.group16.controller.config.GameConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * EndPanel represents the end-game screen UI. It displays a background,
 * a "Game Over" message, and options to retry or escape.
 *
 * Implements {@link Updatable} to be compatible with the game's update loop,
 * although it does not update anything per frame.
 */
public class EndPanel extends JPanel implements Updatable {

    private final BufferedImage backGroundImage;
    private final BufferedImage gameOverImage;
    private final BufferedImage retry;
    private final BufferedImage escape;

    /**
     * Constructs the EndPanel and loads all required image assets.
     */
    public EndPanel() {
        setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setDoubleBuffered(true);

        try {
            backGroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Ending_Screen/SkeletonBackGround.png")));
            gameOverImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Ending_Screen/Game_Over.png")));
            retry = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Ending_Screen/Retry.jpg")));
            escape = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/Ending_Screen/Escape.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrapper method to externally trigger a repaint using the passed Graphics2D object.
     *
     * @param g2 The graphics context to draw with.
     */
    public void draw(Graphics2D g2) {
        paintComponent(g2);
    }

    /**
     * Called once per frame by the game loop. This panel does not animate,
     * so the method is intentionally left empty.
     *
     * @param deltaTime The time passed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
    }

    /**
     * Custom rendering logic for the end panel.
     * Paints the background, game over banner, and action buttons.
     *
     * @param g The graphics context used to draw.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // 1) Draw full-screen island background
        g2.drawImage(backGroundImage, 0, 0,
                GameConfig.SCREEN_WIDTH,
                GameConfig.SCREEN_HEIGHT,
                null);

        // 2) Scale and draw the “Game Over” title at the top center
        int scaledWidth = gameOverImage.getWidth();
        int scaledHeight = (int) (2 * gameOverImage.getHeight() / 2.5);
        int tx = (GameConfig.SCREEN_WIDTH - scaledWidth) / 2;
        int ty = (GameConfig.SCREEN_HEIGHT - scaledHeight) / 2 - 150;
        g2.drawImage(gameOverImage, tx, ty, scaledWidth, scaledHeight, null);

        // 3) Draw the different actions possibility
        g2.drawImage(retry, 75, GameConfig.SCREEN_HEIGHT / 2 + 40, 125, 125, null);
        g2.drawImage(escape, 225, GameConfig.SCREEN_HEIGHT / 2 + 40, 125, 125, null);
    }
}
