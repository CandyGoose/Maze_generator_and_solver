package backend.academy.renderers;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.models.SurfaceType;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ConsoleRendererTest {

    @Test
    public void testRenderMaze() {
        Maze maze = createSimpleMaze();
        ConsoleRenderer renderer = new ConsoleRenderer();
        String output = renderer.render(maze);

        assertNotNull(output, "Рендер должен вернуть строку.");
        assertFalse(output.isEmpty(), "Рендер лабиринта не должен быть пустым.");
    }

    @Test
    public void testRenderMazeWithPath() {
        Maze maze = createSimpleMaze();
        ConsoleRenderer renderer = new ConsoleRenderer();

        List<Coordinate> path = List.of(new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3));
        String output = renderer.render(maze, path);

        assertNotNull(output, "Рендер с путём должен вернуть строку.");
        assertTrue(output.contains("🟨"), "Должны быть показаны жёлтые клетки (путь).");
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
