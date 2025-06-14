package com.group16.controller;

import com.group16.controller.config.GameConfig;
import com.group16.controller.io.KeyHandler;
import com.group16.controller.scene.GameScene;
import com.group16.controller.scene.Scene;
import com.group16.controller.scene.TitleScene;
import com.group16.controller.scene.EndScene;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * The GameController is responsible for initializing the application window,
 * managing the main game loop, and coordinating scene switching.
 */
public class GameController {

    private final JFrame frame;
    private final SceneManager sceneManager;
    private final KeyHandler keyHandler;

    private volatile boolean running;
    private Thread gameThread;

    private static final double NANOS_IN_SECOND = 1_000_000_000.0;

    /**
     * Constructs the GameController, initializes scenes, and sets up the frame and input handling.
     *
     * @throws IOException if there is an issue loading the game scene
     */
    public GameController() throws IOException {
        // Views Initialization
        frame = new JFrame("JavIsland");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        frame.setResizable(false);

        // Scene setup
        sceneManager = new SceneManager();
        sceneManager.add("title", new TitleScene(sceneManager));
        sceneManager.add("game", new GameScene(sceneManager));
        sceneManager.add("end", new EndScene(sceneManager));

        // Input handling
        keyHandler = new KeyHandler(sceneManager);
        frame.addKeyListener(keyHandler);
    }

    /**
     * Starts the game loop and displays the initial scene.
     */
    public void start() {
        sceneManager.switchTo("title");

        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        running = true;
        gameThread = new Thread(() -> {
            long last = System.nanoTime();

            while (running) {
                long now = System.nanoTime();
                float dt = (float) ((now - last));
                last = now;

                Scene currentScene = sceneManager.getCurrent();
                if (currentScene != null) {
                    currentScene.update(dt);

                    SwingUtilities.invokeLater(() -> {
                        JPanel currentPanel = currentScene.getPanel();
                        Container contentPane = frame.getContentPane();

                        if (contentPane.getComponentCount() == 0 || contentPane.getComponent(0) != currentPanel) {
                            contentPane.removeAll();
                            contentPane.add(currentPanel);
                            frame.revalidate();
                            frame.repaint();
                        }
                        currentPanel.repaint();
                    });
                } else {
                    System.err.println("Error: No current scene is set in SceneManager.");
                    stop();
                }

                float wait = GameConfig.FRAME_DURATION - (System.nanoTime() - now);
                try {
                    Thread.sleep((long) (wait / 1_000_000), (int) (wait % 1_000_000));
                } catch (InterruptedException ignored) {
                }
            }
        }, "GameLoopThread");

        gameThread.start();
    }

    /**
     * Stops the game loop thread gracefully.
     */
    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
