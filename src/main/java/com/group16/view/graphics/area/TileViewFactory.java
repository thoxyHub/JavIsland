package com.group16.view.graphics.area;

import com.group16.model.area.tiles.Tile;
import com.group16.model.area.tiles.TileType;

/**
 * Factory class responsible for generating a 2D array of TileViews based on a Tile map.
 * It assigns visual representations (sprites) for tiles based on their type and neighbors.
 */
public class TileViewFactory {

    private static final float GRASS_BASE_CHANCE = 0.65f; // Threshold for selecting main grass tile variant

    private final int mapBorder;
    private final int mapHeight;
    private final int mapWidth;

    private final Tile[][] tileMap;
    private final TileView[][] tileViews;

    /**
     * Private constructor to enforce static factory pattern.
     *
     * @param tileMap   2D array of Tile objects
     * @param mapBorder border size around the map
     */
    private TileViewFactory(Tile[][] tileMap, int mapBorder) {
        this.mapBorder = mapBorder;
        this.mapHeight = tileMap.length;
        this.mapWidth = tileMap[0].length;
        this.tileMap = tileMap;
        this.tileViews = new TileView[mapHeight][mapWidth];
    }

    /**
     * Public factory method to create the 2D TileView map.
     *
     * @param tileMap   tile data to interpret
     * @param mapBorder the border padding used in tile computation
     * @return a TileView 2D array based on the tile types
     */
    public static TileView[][] createTileViewMap(Tile[][] tileMap, int mapBorder) {
        return new TileViewFactory(tileMap, mapBorder).generateTileViews();
    }

    /**
     * Main tile interpretation and sprite assignment logic.
     *
     * @return the 2D TileView array corresponding to the tile map
     */
    private TileView[][] generateTileViews() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                Tile tile = tileMap[i][j];
                tileViews[i][j] = switch (tile.getType()) {
                    case WATER -> Math.random() < 0.95 ? TileView.WATER_MIDDLE : TileView.WATER_MIDDLE_WAVE;
                    case SAND  -> TileView.SAND_MIDDLE;
                    case HILL  -> determineType(countHillNeighbors(i, j));
                    default -> {
                        // Randomize grass style for visual variety
                        float rand = (float) Math.random();
                        if (rand < GRASS_BASE_CHANCE) yield TileView.GRASS_MIDDLE;
                        else if (rand < 0.9) yield TileView.GRASS_WEED;
                        else if (rand < 0.95) yield TileView.GRASS_RED_FLOWER;
                        else yield TileView.GRASS_YELLOW_FLOWER;
                    }
                };
            }
        }
        return tileViews;
    }

    /**
     * Count the water neighbors around a hill tile and generate a bitmask.
     * The bitmask is used to identify a matching hill sprite.
     *
     * @param i tile row index
     * @param j tile column index
     * @return a bitmask of adjacent WATER tiles
     */
    private int countHillNeighbors(int i, int j) {
        int bitmask = 0;
        int[][] dirs = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1},   // 4 cardinal directions
                {-1, 1}, {-1, -1}, {1, 1}, {1, -1}  // diagonals
        };

        int k = 1, count = 0;

        for (int[] d : dirs) {
            if (count == 4 && bitmask != 0) break;

            int nx = i + d[0], ny = j + d[1];
            if (inBounds(nx, ny) && tileMap[nx][ny].getType() == TileType.WATER) {
                bitmask |= k;
            }

            count++;
            k <<= 1;
        }

        return bitmask;
    }

    /**
     * Checks whether the tile coordinates are within the visible/active bounds of the map.
     */
    private boolean inBounds(int x, int y) {
        return x >= mapBorder - 1 && x < mapHeight - mapBorder + 1 &&
                y >= mapBorder - 1 && y < mapWidth - mapBorder + 1;
    }

    /**
     * Maps a bitmask (derived from neighbors) to a specific hill tile sprite.
     *
     * @param mask integer bitmask representing surrounding water tiles
     * @return a corresponding TileView enum value
     */
    private TileView determineType(int mask) {
        return switch (mask) {
            case 0b0000001 -> TileView.HILL_34;
            case 0b0000010 -> TileView.HILL_23;
            case 0b0000100 -> TileView.HILL_12;
            case 0b0001000 -> TileView.HILL_14;
            case 0b0000011 -> TileView.HILL_3;
            case 0b0001001 -> TileView.HILL_4;
            case 0b0000110 -> TileView.HILL_2;
            case 0b0001100 -> TileView.HILL_1;
            case 0b0010000 -> TileView.HILL_234;
            case 0b0100000 -> TileView.HILL_134;
            case 0b01000000 -> TileView.HILL_123;
            case 0b10000000 -> TileView.HILL_124;
            default -> TileView.GRASS_MIDDLE;
        };
    }
}
