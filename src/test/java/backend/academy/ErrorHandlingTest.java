package backend.academy;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import backend.academy.solvers.BFSSolver;
import backend.academy.solvers.AStarSolver;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingTest {
    @Test
    public void testNoPathExists() {
        Maze maze = createBlockedMaze();
        BFSSolver solver = new BFSSolver();

        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(3, 3);

        List<Coordinate> path = solver.solve(maze, start, end);

        assertTrue(path.isEmpty(), "Путь не должен существовать.");
    }

    private Maze createBlockedMaze() {
        Maze maze = new Maze(5, 5);
        Cell[][] grid = maze.getGrid();
        // Все стены
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL, SurfaceType.NORMAL);
            }
        }
        return maze;
    }
}
