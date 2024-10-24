package backend.academy.solvers;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BFSSolverTest {

    @Test
    public void testBFSSolvePathExists() {
        Maze maze = createSimpleMaze();
        BFSSolver solver = new BFSSolver();

        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(5, 5);

        List<Coordinate> path = solver.solve(maze, start, end);

        assertFalse(path.isEmpty(), "Должен быть найден путь.");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка пути должна совпадать с точкой B.");
    }

    private Maze createSimpleMaze() {
        Maze maze = new Maze(7, 7);
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                maze.getGrid()[row][col] = new Cell(row, col, Cell.Type.PASSAGE, SurfaceType.NORMAL);
            }
        }
        return maze;
    }
}
