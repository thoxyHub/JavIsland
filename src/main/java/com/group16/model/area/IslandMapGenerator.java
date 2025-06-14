package com.group16.model.area;

import com.group16.controller.config.GameConfig;
import com.group16.model.area.tiles.Tile;
import com.group16.model.area.tiles.TileType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generates a tile-based island map using data loaded from a file.
 * Adds a configurable water border around the map and computes "hill" tiles
 * based on adjacency to water.
 */
public class IslandMapGenerator {

    // Map dimensions and player spawn config
    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;

    public static final int MAP_BORDER = 7;
    private static final int PLAYER_START_X = 10;
    private static final int PLAYER_START_Y = 10;

    private Tile[][] tileMap;

    /**
     * Constructs the island map using a tile definition from file.
     *
     * @param fileName the path to the file containing tile type values
     * @throws IOException if reading from the file fails
     */
    public IslandMapGenerator(String fileName) throws IOException {
        int[][] fileMap = loadTableFromFile(fileName);
        Objects.requireNonNull(fileMap);

        MAP_WIDTH = fileMap[0].length + (2 * MAP_BORDER);
        MAP_HEIGHT = fileMap.length + (2 * MAP_BORDER);

        tileMap = new Tile[MAP_HEIGHT][MAP_WIDTH];

        createMap(fileMap);
        computeHill();
    }

    /**
     * Returns a defensive copy of the generated map.
     *
     * @return a 2D Tile array representing the map
     */
    public Tile[][] getMap() {
        Tile[][] tileCopyMap = new Tile[MAP_HEIGHT][MAP_WIDTH];

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                tileCopyMap[i][j] = new Tile(tileMap[i][j].getType());
            }
        }

        return tileCopyMap;
    }

    /**
     * Returns the starting X pixel position of the player (based on config).
     *
     * @return starting X position
     */
    public int getStartX() {
        return GameConfig.TILE_SIZE * (MAP_BORDER + PLAYER_START_X);
    }

    /**
     * Returns the starting Y pixel position of the player (based on config).
     *
     * @return starting Y position
     */
    public int getStartY() {
        return GameConfig.TILE_SIZE * (MAP_BORDER + PLAYER_START_Y);
    }

    /**
     * Builds the internal tile map from the raw tile type grid loaded from file.
     * Adds water borders on all sides.
     *
     * @param fileMap the raw tile type grid
     * @throws IOException should tile creation fail (though unlikely)
     */
    private void createMap(int[][] fileMap) throws IOException {

        // Top water border
        for (int y = 0; y < MAP_BORDER; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                tileMap[y][x] = new Tile(TileType.WATER);
            }
        }

        // Middle: file data + vertical water border
        for (int y = MAP_BORDER; y < fileMap.length + MAP_BORDER; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (x < MAP_BORDER || x >= fileMap[0].length + MAP_BORDER) {
                    tileMap[y][x] = new Tile(TileType.WATER);
                } else {
                    // Construct the tiles that are on the file MapDesign1.txt
                    Tile tile = new Tile(TileType.values()[fileMap[y - MAP_BORDER][x - MAP_BORDER]]);
                    // randomGenerationOfElements(tile);
                    tileMap[y][x] = tile;
                }
            }
        }

        // Bottom water border
        for (int y = 0; y < MAP_BORDER; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                tileMap[y + fileMap.length + MAP_BORDER][x] = new Tile(TileType.WATER);
            }
        }
    }

    /**
     * Determines the tile type for a GRASS tile based on adjacent water bitmask.
     * Used to convert grass into HILL if close to water.
     *
     * @param mask bitmask indicating neighboring water tiles
     * @return the resulting tile type
     */
    private TileType determineType(int mask) {
        return switch (mask) {
            case 0b0000001, 0b0000010, 0b0000100, 0b0001000,
                 0b0000011, 0b0001001, 0b0000110, 0b0001100,
                 0b0010000, 0b0100000, 0b01000000, 0b10000000 -> TileType.HILL;
            default -> TileType.GRASS;
        };
    }

    /**
     * Counts cardinal and diagonal neighbors that match the given tile type
     * and generates a bitmask used in terrain classification.
     *
     * @param i    the row index of the tile
     * @param j    the column index of the tile
     * @param type the tile type to match
     * @return the generated bitmask of matching neighbors
     */
    private int countAllCardinalNeighbors(int i, int j, TileType type) {
        int bitmask = 0;

        // All possible 8 directions (4 cardinal + 4 diagonal)
        int[][] dirs = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1},
                {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
        };

        int k = 1, count = 0;

        for (int[] d : dirs) {
            if (count == 4 && bitmask != 0) break;

            int nx = i + d[0], ny = j + d[1];

            if (inBounds(nx, ny) && tileMap[nx][ny].getType() == type) {
                bitmask |= k;
            }

            count++;
            k = k << 1;
        }

        return bitmask;
    }

    /**
     * Checks if a given tile coordinate is within valid tile bounds
     * excluding the map border.
     *
     * @param x row index
     * @param y column index
     * @return true if in bounds
     */
    private boolean inBounds(int x, int y) {
        return x >= MAP_BORDER - 1 && x < MAP_HEIGHT - MAP_BORDER + 1 &&
                y >= MAP_BORDER - 1 && y < MAP_WIDTH - MAP_BORDER + 1;
    }

    /**
     * Converts some GRASS tiles into HILLs based on water proximity.
     * Uses bitmasking of neighboring water tiles.
     */
    public void computeHill() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (tileMap[i][j].getType() == TileType.GRASS) {
                    int mask = countAllCardinalNeighbors(i, j, TileType.WATER);
                    tileMap[i][j] = new Tile(determineType(mask));
                }
            }
        }
    }

    /**
     * Loads a grid of tile type integers from a whitespace-separated file.
     *
     * @param filename path to the file
     * @return a 2D array of integers
     * @throws IOException if reading fails
     */
    public static int[][] loadTableFromFile(String filename) throws IOException {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                int[] row = new int[tokens.length];

                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }

                rows.add(row);
            }
        }

        return rows.toArray(new int[0][]);
    }
}
