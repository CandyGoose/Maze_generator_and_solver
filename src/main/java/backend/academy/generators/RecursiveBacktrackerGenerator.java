package backend.academy.generators;

import backend.academy.interfaces.Generator;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrackerGenerator implements Generator {

    private static final int WALL_STEP = 2;
    private final Random random = new Random();

    @Override
    public Maze generate(int initialHeight, int initialWidth) {
        int height = initialHeight % 2 == 0 ? initialHeight + 1 : initialHeight;
        int width = initialWidth % 2 == 0 ? initialWidth + 1 : initialWidth;

        Maze maze = new Maze(height, width);
        Stack<Coordinate> stack = new Stack<>();
        int startRow = 1;
        int startCol = 1;
        maze.getGrid()[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE, maze.getRandomSurface());
        stack.push(new Coordinate(startRow, startCol));

        while (!stack.isEmpty()) {
            Coordinate current = stack.peek();
            List<Coordinate> neighbors = getUnvisitedNeighbors(maze, current);

            if (!neighbors.isEmpty()) {
                Coordinate chosen = neighbors.get(random.nextInt(neighbors.size()));
                maze.removeWall(current, chosen);
                maze.getGrid()[chosen.row()][chosen.col()] = new Cell(chosen.row(), chosen.col(),
                    Cell.Type.PASSAGE, maze.getRandomSurface());
                stack.push(chosen);
            } else {
                stack.pop();
            }
        }

        maze.addCycles();

        return maze;
    }

    private List<Coordinate> getUnvisitedNeighbors(Maze maze, Coordinate coord) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {{-WALL_STEP, 0}, {WALL_STEP, 0}, {0, -WALL_STEP}, {0, WALL_STEP}};

        for (int[] dir : directions) {
            int newRow = coord.row() + dir[0];
            int newCol = coord.col() + dir[1];
            if (maze.isInBounds(newRow, newCol) && maze.getGrid()[newRow][newCol].type() == Cell.Type.WALL) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }

        Collections.shuffle(neighbors, random);
        return neighbors;
    }
}
