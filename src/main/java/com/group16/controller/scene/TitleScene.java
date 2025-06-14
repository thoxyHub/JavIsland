package com.group16.controller.scene;

import com.group16.view.ui.TitlePanel;
import com.group16.controller.SceneManager;
import com.group16.model.TitleLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The TitleScene class handles the main menu scene of the game.
 * It allows users to navigate between "Start", "Map Selection", and back using the keyboard.
 * The scene transitions to the game scene upon map selection.
 */
public final class TitleScene implements Scene {

    private final SceneManager sceneManager;
    private final TitlePanel panel;
    private final TitleLogic model;

    // Input cooldowns to avoid unintended repeated key handling
    private static final long ARROW_COOLDOWN = 300;
    private static final long ENTER_COOLDOWN = 300;

    private long lastEnterPressTime = 0;
    private long lastUpPressTime = 0;
    private long lastDownPressTime = 0;

    /**
     * Constructs the TitleScene with its logic model and UI panel.
     *
     * @param sm The SceneManager to handle transitions between scenes
     */
    public TitleScene(SceneManager sm) {
        this.sceneManager = sm;
        this.model = new TitleLogic();
        this.panel = new TitlePanel();
        this.model.registerObserver(panel);
        this.model.notifyObserver();
    }

    /**
     * Returns the JPanel representing this scene's UI.
     *
     * @return the title screen panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Update logic for the title scene (not used currently).
     *
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
    }

    /**
     * Renders the title panel.
     *
     * @param g2 the Graphics2D context to draw on
     */
    @Override
    public void draw(Graphics2D g2) {
        panel.paint(g2);
    }

    /**
     * Handles key press events for menu navigation and selection.
     *
     * @param e the key event
     */
    public void onKeyPressed(KeyEvent e) {
        long currentTime = System.currentTimeMillis();

        switch (model.getMenuState()) {
            case START:
                // Transition to map selection menu
                if (e.getKeyCode() == KeyEvent.VK_ENTER && currentTime - lastEnterPressTime > ENTER_COOLDOWN) {
                    lastEnterPressTime = currentTime;
                    model.setMenuState(TitleLogic.MenuState.MAP_SELECT);
                    model.setSelectedOption(0);
                }
                break;

            case MAP_SELECT:
                // Navigate up through map options
                if (e.getKeyCode() == KeyEvent.VK_UP && currentTime - lastUpPressTime > ARROW_COOLDOWN) {
                    lastUpPressTime = currentTime;
                    int selectedOption = (model.getSelectedOption() - 1 + 3) % 3;
                    model.setSelectedOption(selectedOption);
                }
                // Navigate down through map options
                else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentTime - lastDownPressTime > ARROW_COOLDOWN) {
                    lastDownPressTime = currentTime;
                    int selectedOption = (model.getSelectedOption() + 1) % 3;
                    model.setSelectedOption(selectedOption);
                }
                // Handle map selection or return to start menu
                else if (e.getKeyCode() == KeyEvent.VK_ENTER && currentTime - lastEnterPressTime > ENTER_COOLDOWN) {
                    lastEnterPressTime = currentTime;
                    if (model.getSelectedOption() == 0) {
                        sceneManager.switchTo("game");
                        if (sceneManager.getCurrent() instanceof GameScene) {
                            ((GameScene) sceneManager.getCurrent()).setMapSelection(1);
                        }
                    } else if (model.getSelectedOption() == 1) {
                        sceneManager.switchTo("game");
                        if (sceneManager.getCurrent() instanceof GameScene) {
                            ((GameScene) sceneManager.getCurrent()).setMapSelection(2);
                        }
                    } else if (model.getSelectedOption() == 2) {
                        model.setMenuState(TitleLogic.MenuState.START);
                    }
                }
                break;
        }
    }

    /**
     * Currently no action needed on key release.
     *
     * @param e the key event
     */
    public void onKeyReleased(KeyEvent e) {
        // No action needed on key release
    }

    /**
     * Currently no action needed on key typed.
     *
     * @param e the key event
     */
    public void onKeyTyped(KeyEvent e) {
        // No action needed on key typed
    }
}
