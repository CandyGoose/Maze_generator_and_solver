package backend.academy;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import backend.academy.solvers.AStarSolver;
import backend.academy.solvers.BFSSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnownMazeSolverTest {

    @Test
    public void testBFSSolveKnownMaze() {
        Maze maze = createKnownMaze();
        BFSSolver solver = new BFSSolver();

        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(3, 3);

        List<Coordinate> path = solver.solve(maze, start, end);

        assertEquals(5, path.size(), "Путь должен содержать 5 шагов.");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка пути должна совпадать с точкой Б.");
    }

    @Test
    public void testAStarSolveKnownMaze() {
        Maze maze = createKnownMaze();
        AStarSolver solver = new AStarSolver();

        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(3, 3);

        List<Coordinate> path = solver.solve(maze, start, end);

        assertEquals(5, path.size(), "Путь должен содержать 5 шагов.");
        assertEquals(end, path.get(path.size() - 1), "Конечная точка пути должна совпадать с точкой Б.");
    }

    private Maze createKnownMaze() {
        Maze maze = new Maze(5, 5);
        Cell[][] grid = maze.getGrid();
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL, SurfaceType.NORMAL);
            }
        }

        grid[1][1] = new Cell(1, 1, Cell.Type.PASSAGE, SurfaceType.NORMAL);
        grid[1][2] = new Cell(1, 2, Cell.Type.PASSAGE, SurfaceType.NORMAL);
        grid[2][2] = new Cell(2, 2, Cell.Type.PASSAGE, SurfaceType.NORMAL);
        grid[3][2] = new Cell(3, 2, Cell.Type.PASSAGE, SurfaceType.NORMAL);
        grid[3][3] = new Cell(3, 3, Cell.Type.PASSAGE, SurfaceType.NORMAL);

        return maze;
    }
}
