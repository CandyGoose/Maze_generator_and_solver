package backend.academy.models;

import java.util.Random;

public final class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;
    private final Random random = new Random();

    private static final int SWAMP_CHANCE = 5;
    private static final int SAND_CHANCE = 15;
    private static final int COIN_CHANCE = 20;
    private static final int ROAD_CHANCE = 30;
    private static final int MAX_CHANCE = 100;

    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL, SurfaceType.NORMAL);
            }
        }
    }

    private SurfaceType getRandomSurface() {
        int chance = random.nextInt(MAX_CHANCE);
        SurfaceType surfaceType;
        if (chance < SWAMP_CHANCE) {
            surfaceType = SurfaceType.SWAMP;
        } else if (chance < SAND_CHANCE) {
            surfaceType = SurfaceType.SAND;
        } else if (chance < COIN_CHANCE) {
            surfaceType = SurfaceType.COIN;
        } else if (chance < ROAD_CHANCE) {
            surfaceType = SurfaceType.ROAD;
        } else {
            surfaceType = SurfaceType.NORMAL;
        }
        return surfaceType;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
