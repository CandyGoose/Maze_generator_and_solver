package backend.academy.generators;

import backend.academy.interfaces.Generator;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimGenerator implements Generator {

    private final Random random = new Random();
    private static final int CHANCE_MAX = 100;
    private static final int SWAMP_CHANCE = 5;
    private static final int SAND_CHANCE = 15;
    private static final int COIN_CHANCE = 20;
    private static final int ROAD_CHANCE = 30;

    @Override
    public Maze generate(int height, int width) {
        int adjustedHeight = (height % 2 == 0) ? height + 1 : height;
        int adjustedWidth = (width % 2 == 0) ? width + 1 : width;

        Maze maze = new Maze(adjustedHeight, adjustedWidth);
        List<Coordinate> wallList = new ArrayList<>();

        int startRow = 1;
        int startCol = 1;
        maze.getGrid()[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE, getRandomSurface());

        addWalls(maze, startRow, startCol, wallList);

        while (!wallList.isEmpty()) {
            int randIndex = random.nextInt(wallList.size());
            Coordinate wall = wallList.remove(randIndex);

            List<Coordinate> adjacentCells = getAdjacentCells(maze, wall);

            if (adjacentCells.size() != 2) {
                continue;
            }

            Coordinate cell1 = adjacentCells.get(0);
            Coordinate cell2 = adjacentCells.get(1);

            boolean cell1InMaze = maze.getGrid()[cell1.row()][cell1.col()].type() == Cell.Type.PASSAGE;
            boolean cell2InMaze = maze.getGrid()[cell2.row()][cell2.col()].type() == Cell.Type.PASSAGE;

            if (cell1InMaze ^ cell2InMaze) {
                Coordinate newCell = cell1InMaze ? cell2 : cell1;

                maze.getGrid()[wall.row()][wall.col()] = new Cell(wall.row(), wall.col(),
                    Cell.Type.PASSAGE, getRandomSurface());
                maze.getGrid()[newCell.row()][newCell.col()] = new Cell(newCell.row(), newCell.col(),
                    Cell.Type.PASSAGE, getRandomSurface());

                addWalls(maze, newCell.row(), newCell.col(), wallList);
            }
        }

        return maze;
    }

    private void addWalls(Maze maze, int row, int col, List<Coordinate> wallList) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isInBounds(maze, newRow, newCol) && maze.getGrid()[newRow][newCol].type() == Cell.Type.WALL) {
                Coordinate wall = new Coordinate(newRow, newCol);
                if (!wallList.contains(wall)) {
                    wallList.add(wall);
                }
            }
        }
    }

    private List<Coordinate> getAdjacentCells(Maze maze, Coordinate wall) {
        List<Coordinate> cells = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int adjRow = wall.row() + dir[0];
            int adjCol = wall.col() + dir[1];
            if (isInBounds(maze, adjRow, adjCol) && isCell(wall, dir)) {
                cells.add(new Coordinate(adjRow, adjCol));
            }
        }
        return cells;
    }

    private boolean isCell(Coordinate wall, int[] dir) {
        return (wall.row() + dir[0]) % 2 != 0 && (wall.col() + dir[1]) % 2 != 0;
    }

    private boolean isInBounds(Maze maze, int row, int col) {
        boolean isRowInBounds = row > 0 && row < maze.getHeight() - 1;
        boolean isColInBounds = col > 0 && col < maze.getWidth() - 1;

        return isRowInBounds && isColInBounds;
    }

    private SurfaceType getRandomSurface() {
        SurfaceType surfaceType;
        int chance = random.nextInt(CHANCE_MAX);

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
}
