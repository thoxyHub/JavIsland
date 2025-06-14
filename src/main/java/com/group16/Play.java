package com.group16;

import com.group16.controller.GameController;

import javax.swing.*;
import java.io.IOException;

/**
 * Entry point for the JavIsland game.
 * <p>
 * Initializes and starts the game controller using Swing's event-dispatching thread.
 */
public class Play {

    /**
     * The main method that launches the game.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if an error occurs during game setup or resource loading
     */
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                GameController controller = new GameController();
                controller.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
