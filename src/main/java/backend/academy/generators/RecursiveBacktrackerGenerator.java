package backend.academy.generators;

import backend.academy.interfaces.Generator;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrackerGenerator implements Generator {

    private static final int MIN_SURFACE_CHANCE = 5;
    private static final int SAND_CHANCE = 15;
    private static final int COIN_CHANCE = 20;
    private static final int ROAD_CHANCE = 30;
    private static final int CHANCE_MAX = 100;
    private static final int WALL_STEP = 2;

    private final Random random = new Random();

    @Override
    public Maze generate(int initialHeight, int initialWidth) {
        int height = initialHeight;
        int width = initialWidth;

        if (height % 2 == 0) {
            height += 1;
        }
        if (width % 2 == 0) {
            width += 1;
        }

        Maze maze = new Maze(height, width);
        Stack<Coordinate> stack = new Stack<>();
        int startRow = 1;
        int startCol = 1;
        maze.getGrid()[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE,
            getRandomSurface());
        stack.push(new Coordinate(startRow, startCol));

        while (!stack.isEmpty()) {
            Coordinate current = stack.peek();
            List<Coordinate> neighbors = getUnvisitedNeighbors(maze, current);

            if (!neighbors.isEmpty()) {
                Coordinate chosen = neighbors.get(random.nextInt(neighbors.size()));
                removeWall(maze, current, chosen);
                maze.getGrid()[chosen.row()][chosen.col()] = new Cell(chosen.row(), chosen.col(),
                    Cell.Type.PASSAGE, getRandomSurface());
                stack.push(chosen);
            } else {
                stack.pop();
            }
        }

        return maze;
    }

    private List<Coordinate> getUnvisitedNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {{-WALL_STEP, 0}, {WALL_STEP, 0}, {0, -WALL_STEP}, {0, WALL_STEP}};

        for (int[] dir : directions) {
            int newRow = coord.row() + dir[0];
            int newCol = coord.col() + dir[1];
            if (isInBounds(maze, newRow, newCol) && maze.getGrid()[newRow][newCol].type() == Cell.Type.WALL) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }

        Collections.shuffle(neighbors, random);
        return neighbors;
    }

    private boolean isInBounds(Maze maze, int row, int col) {
        return row > 0 && row < maze.getHeight() - 1 && col > 0 && col < maze.getWidth() - 1;
    }

    private void removeWall(Maze maze, Coordinate current, Coordinate chosen) {
        int wallRow = (current.row() + chosen.row()) / 2;
        int wallCol = (current.col() + chosen.col()) / 2;
        maze.getGrid()[wallRow][wallCol] = new Cell(wallRow, wallCol, Cell.Type.PASSAGE, getRandomSurface());
    }

    private SurfaceType getRandomSurface() {
        int chance = random.nextInt(CHANCE_MAX);
        SurfaceType surfaceType;

        if (chance < MIN_SURFACE_CHANCE) {
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
