package backend.academy.generators;

import backend.academy.interfaces.Generator;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;

import java.util.*;

public class RecursiveBacktrackerGenerator implements Generator {
    private final Random random = new Random();

    @Override
    public Maze generate(int height, int width) {
        if (height % 2 == 0) height += 1;
        if (width % 2 == 0) width += 1;

        Maze maze = new Maze(height, width);
        Stack<Coordinate> stack = new Stack<>();
        int startRow = 1;
        int startCol = 1;
        maze.getGrid()[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE, getRandomSurface());
        stack.push(new Coordinate(startRow, startCol));

        while (!stack.isEmpty()) {
            Coordinate current = stack.peek();
            List<Coordinate> neighbors = getUnvisitedNeighbors(maze, current);

            if (!neighbors.isEmpty()) {
                Coordinate chosen = neighbors.get(random.nextInt(neighbors.size()));
                removeWall(maze, current, chosen);
                maze.getGrid()[chosen.row()][chosen.col()] = new Cell(chosen.row(), chosen.col(), Cell.Type.PASSAGE, getRandomSurface());
                stack.push(chosen);
            } else {
                stack.pop();
            }
        }

        return maze;
    }

    private List<Coordinate> getUnvisitedNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 } };

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
}
