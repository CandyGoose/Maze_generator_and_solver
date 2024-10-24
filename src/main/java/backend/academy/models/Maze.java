package backend.academy.models;

import java.util.Random;

public final class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;
    private final Random random = new Random();

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

    public void setPassageWithSurface(int row, int col) {
        grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE, getRandomSurface());
    }

    private SurfaceType getRandomSurface() {
        int chance = random.nextInt(100);
        if (chance < 5) {
            return SurfaceType.SWAMP;
        } else if (chance < 15) {
            return SurfaceType.SAND;
        } else if (chance < 20) {
            return SurfaceType.COIN;
        } else if (chance < 30) {
            return SurfaceType.ROAD;
        } else {
            return SurfaceType.NORMAL;
        }
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
