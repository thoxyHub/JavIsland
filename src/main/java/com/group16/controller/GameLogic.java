package com.group16.controller;

import com.group16.model.entity.GameEntity;
import com.group16.model.area.Island;
import com.group16.model.entity.actors.Mobs;
import com.group16.model.entity.actors.Player;
import com.group16.model.entity.elements.Rock;
import com.group16.model.entity.elements.Tree;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;
import com.group16.model.utils.maths.Vector;
import com.group16.model.Subject;
import com.group16.view.Observer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameLogic is the central controller for game state progression,
 * including wave logic, resource generation, player input handling,
 * and observer notification.
 */
public class GameLogic implements Subject {

    private final Island island;
    private final Player player;

    private static final float PREPARATION_TIMER = 30.0f;
    private static final float WAVE_DISPLAY_TIMER = 2.0f;
    private static final float NANOS_TO_SECONDS = 1E9f;
    private static final int START_WAVE_NUMBER = 1;

    private static final int BASE_MOB_COUNT = 4;
    private static final double TREE_SPAWN_PROBABILITY = 0.8;
    private static final int BASE_RESOURCE_FACTOR = 15;
    private static final int BASE_RESOURCE_OFFSET = 5;

    private boolean isGameOver = false;
    private boolean isPreparing = false;

    private float prepTimer = PREPARATION_TIMER;
    private float waveDisplayTimer = WAVE_DISPLAY_TIMER;
    private int waveNumber = START_WAVE_NUMBER;

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Constructs the game logic engine with the given island and player.
     */
    public GameLogic(Island island, Player player) {
        this.island = island;
        this.player = player;
    }

    /**
     * Updates the game state including waves, preparation, and entity updates.
     *
     * @param dt delta time in nanoseconds
     */
    public void update(float dt) {
        if (waveDisplayTimer > 0) {
            waveDisplayTimer -= dt / NANOS_TO_SECONDS;
        }

        if (player.getHealth() <= 0) {
            isGameOver = true;
        } else {
            if (isPreparing) {
                prepTimer -= dt / NANOS_TO_SECONDS;
                if (prepTimer <= 0) startWave();
            }

            island.update(dt);

            if (!isPreparing && !island.stillMobsAlive()) {
                startPreparation();
            }
        }
        notifyObserver();
    }

    /**
     * Begins the preparation phase before a wave starts.
     */
    private void startPreparation() {
        isPreparing = true;
        prepTimer = PREPARATION_TIMER;
        generateResources();
        island.updateNewRound();
    }

    /**
     * Begins a new wave by spawning mobs and giving the player ammo.
     */
    private void startWave() {
        System.out.println("Wave " + waveNumber + " starting!");
        isPreparing = false;
        waveDisplayTimer = WAVE_DISPLAY_TIMER;
        spawnMobs();
        waveNumber++;
        player.getInventory().add(new Resources(ResourceType.AMMO, waveNumber * 5));
        island.updateNewRound();
    }

    /**
     * Spawns enemies based on wave number using logarithmic scaling.
     */
    private void spawnMobs() {
        int numMobs = (int) (BASE_MOB_COUNT * (Math.log(waveNumber + 1) / Math.log(2)));

        for (int i = 0; i < numMobs; i++) {
            Vector spawnPosition = getRandomSpawnPosition();
            island.addEntity(new Mobs(island, spawnPosition, player));
        }
    }

    /**
     * Generates resources (Tree or Rock) at random positions on the map.
     */
    private void generateResources() {
        int numResources = (int) (BASE_RESOURCE_FACTOR * (Math.log(waveNumber + 1) / Math.log(3)) + BASE_RESOURCE_OFFSET);

        for (int i = 0; i < numResources; i++) {
            Vector resourcePosition = getRandomSpawnPosition();
            GameEntity resource = Math.random() < TREE_SPAWN_PROBABILITY ? new Tree(resourcePosition) : new Rock(resourcePosition);
            island.addEntity(resource);
        }
    }

    /**
     * Finds a random valid (walkable) tile for spawning.
     *
     * @return a random walkable Vector position
     */
    private Vector getRandomSpawnPosition() {
        Vector spawnPosition;
        Random rand = new Random();

        do {
            int x = rand.nextInt(island.getMAP_BORDER(), island.getMAP_WIDTH());
            int y = rand.nextInt(island.getMAP_BORDER(), island.getMAP_HEIGHT());
            spawnPosition = new Vector(x, y);
        } while (!island.getTile(spawnPosition).isWalkable());

        return spawnPosition;
    }

    /**
     * Checks whether the game has ended (player is dead).
     *
     * @return true if game over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Handles key presses and forwards actions to the player.
     *
     * @param e the KeyEvent representing the pressed key
     */
    public void onKeyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            // Movement
            case KeyEvent.VK_UP -> player.moveNorth();
            case KeyEvent.VK_DOWN -> player.moveSouth();
            case KeyEvent.VK_RIGHT -> player.moveEast();
            case KeyEvent.VK_LEFT -> player.moveWest();

            // Actions
            case KeyEvent.VK_S -> player.useSword();
            case KeyEvent.VK_B -> player.buildingWoodBlock();
            case KeyEvent.VK_G -> player.useGun();
            case KeyEvent.VK_N -> player.buildingStoneBlock();
            case KeyEvent.VK_E -> player.openCloseInventory();
        }
    }

    public void onKeyReleased(KeyEvent e) {
        // No-op
    }

    public void onKeyTyped(KeyEvent e) {
        // No-op
    }

    public float getWaveStartDisplayTime() {
        return waveDisplayTimer;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public float getPreparationTime() {
        return prepTimer;
    }

    public boolean getIsPreparing() {
        return isPreparing;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(o -> o.update(this));
    }
}
